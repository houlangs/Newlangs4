package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.apis.Certification;
import com.guaning.newlangs.apis.SMSAPI;
import com.guaning.newlangs.dto.LoginDto;
import com.guaning.newlangs.dto.RegisterDto;
import com.guaning.newlangs.dto.UserUpdateDto;
import com.guaning.newlangs.entity.Config;
import com.guaning.newlangs.entity.DomainRecord;
import com.guaning.newlangs.entity.PointRecord;
import com.guaning.newlangs.entity.User;
import com.guaning.newlangs.mapper.UserMapper;
import com.guaning.newlangs.service.ConfigService;
import com.guaning.newlangs.service.DomainRecordService;
import com.guaning.newlangs.service.PointRecordService;
import com.guaning.newlangs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	final ConfigurableListableBeanFactory beanFactory;
	
	private final RedisTemplate<String, String> redisTemplate;
	public UserServiceImpl(ConfigurableListableBeanFactory beanFactory, RedisTemplate<String, String> redisTemplate) {
		this.beanFactory = beanFactory;
		this.redisTemplate = redisTemplate;
	}
	
	public ConfigService getConfigService() {
		return beanFactory.getBean(ConfigService.class);
	}
	
	public DomainRecordService getDomainRecordService() {
		return beanFactory.getBean(DomainRecordService.class);
	}
	
	public PointRecordService getPointRecordService() {
		return beanFactory.getBean(PointRecordService.class);
	}
	
	private String generateSixDigitCode() {
        // 生成一个随机的 6 位数字验证码
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

	// 邮箱验证码
	@Override
	public SaResult emailCode(String email) {
		// 读取信息
		String host = getConfigService().getById("mail_host").getV();
		Integer port = Integer.valueOf(getConfigService().getById("mail_port").getV());
		String from = getConfigService().getById("mail_from").getV();
		String user = getConfigService().getById("mail_user").getV();
		String pass = getConfigService().getById("mail_pass").getV();
		boolean sslEnable = Boolean.parseBoolean(getConfigService().getById("mail_sslEnable").getV());
		boolean starttlsEnable = Boolean.parseBoolean(getConfigService().getById("mail_starttlsEnable").getV());
		long timeout = Long.parseLong(getConfigService().getById("mail_timeout").getV());
		long connectionTimeout = Long.parseLong(getConfigService().getById("mail_connectionTimeout").getV());

		// 配置信息
		MailAccount account = new MailAccount();
		account.setHost(host);
		account.setPort(port);
		account.setAuth(true);
		account.setFrom(from);
		account.setUser(user);
		account.setPass(pass);
		account.setSslEnable(sslEnable);
		account.setTimeout(timeout);
		account.setConnectionTimeout(connectionTimeout);
		account.setStarttlsEnable(starttlsEnable);

		// 生成6位数字验证码
		String code = generateSixDigitCode();
		log.info("code = {}", code);

		try {
			// 验证码缓存到redis中，并设置有效期两分钟
			redisTemplate.opsForValue().set(email, code, 2, TimeUnit.MINUTES);
		} catch (Exception e) {
			return SaResult.error("redis缓存失败");
		}

		// MailUtil.send(account, CollUtil.newArrayList(email), "NewLangs", "您的验证码为" +
		// code + ",验证码有效期为2分钟,感谢您的注册,使用愉快!", false);

		try {
			// 邮件发送验证码
			MailUtil.send(account, CollUtil.newArrayList(email), "NewLangs",
					"您的邮箱验证码为：" + code + ",验证码有效期为2分钟,感谢您的注册,祝您使用愉快！!", false);
		} catch (Exception e) {
			// 发送失败，删除缓存
			redisTemplate.delete(email);
			return SaResult.error("邮件发送失败");
		}

		return SaResult.ok("邮件发送成功");
	}

	// 发送手机验证码
	@Override
	public SaResult phoneCode(String phone) {
		User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
		if (user != null) {
			return SaResult.error("该手机号码已被注册，请更换手机号码");
		}

		// 生成验证码
		String code = RandomUtil.randomNumbers(6);
		// 缓存手机验证码
		try {
			redisTemplate.opsForValue().set(phone, code, 2, TimeUnit.MINUTES);
		} catch (Exception e) {
			return SaResult.error("redis缓存失败");
		}
		String username = "短信宝账号";
		String APIKEY = "短信宝APIkey";
		String content = "【二级域名】您的验证码为：" + code;
		log.info("code = {}", code);

		String result = SMSAPI.sms(username, APIKEY, phone, content);
		return switch (result) {
			case "0" -> SaResult.ok("短信发送成功，请查收");
			case "30" -> SaResult.error("30 发送失败，请联系管理员");
			case "40" -> SaResult.error("40 发送失败，请联系管理员");
			case "41" -> SaResult.error("41 发送失败，请联系管理员");
			case "43" -> SaResult.error("IP地址受限，请联系管理员");
			case "50" -> SaResult.error("50 发送失败，请联系管理员");
			case "51" -> SaResult.error("51 手机号码不正确");
			default -> null;
		};
	}

	// 校验手机验证码
	@Override
	public SaResult checkPhone(String phone, String code) {
		// 获取缓存的手机验证码
		String codeInRedis;
		try {
			codeInRedis = redisTemplate.opsForValue().get(phone);
		} catch (Exception e) {
			return SaResult.error("redis缓存读取失败");
		}
		// 验证码比对
		// 验证码比对
		if (codeInRedis == null) {
			return SaResult.error("验证码错误");
		}
		if (!codeInRedis.equals(code)) {
			return SaResult.error("验证码错误");
		}

		try {
			// 如果校验成功，删除redis缓存的验证码
			redisTemplate.delete(phone);
		} catch (Exception e) {
			return SaResult.error("redis缓存删除失败");
		}

		long userId = StpUtil.getLoginIdAsLong();
		User user = getById(userId);

		User dto = new User();
		dto.setId(userId);
		dto.setPhone(phone);
		if (user.getIdNumber() == null && user.getName() == null) {
			dto.setRoleId(3);
		}
		dto.setUpdatedTime(LocalDateTime.now());
		updateById(dto);

		return SaResult.ok("校验成功，升级为高级用户");
	}

	// 实名认证
	@Override
	public SaResult certification(String name, String idNumber) throws IOException {
		User idOne = getOne(Wrappers.<User>lambdaQuery().eq(User::getIdNumber, idNumber));
		if (idOne != null) {
			return SaResult.error("该身份信息已被认证，请更换身份信息");
		}

		User user = getById(StpUtil.getLoginIdAsLong());
		if (user.getPhone() == null) {
			return SaResult.error("请先完成手机认证");
		}

		String url = "https://eid.shumaidata.com/eid/check";
		String appCode = "APPcode";
		Map<String, String> params = new HashMap<>();
		params.put("idcard", idNumber);
		params.put("name", name);
		String all = Certification.postForm(appCode, url, params);
		JSONObject object = JSON.parseObject(all);
		JSONObject result = JSON.parseObject(object.getString("result"));
		if (object.getString("code").equals("0")) {
			if (result.getString("res").equals("1")) {
				// 实名成功升级为认证用户
				long userId = StpUtil.getLoginIdAsLong();
				User dto = new User();
				dto.setId(userId);
				dto.setName(name);
				dto.setIdNumber(idNumber);
				dto.setRoleId(4);
				dto.setUpdatedTime(LocalDateTime.now());
				updateById(dto);

				return SaResult.ok("校验成功，升级为认证用户，恭喜您可以使用备案域名了");
			} else {
				return SaResult.error(result.getString("description"));
			}
		} else {
			return SaResult.error(object.getString("message"));
		}
	}

	// 注册
	@Override
	public SaResult register(RegisterDto dto) {
		// 获取参数
		String email = dto.getEmail();
		String password = dto.getPassword();
		String code = dto.getCode();

		// 判断邮箱是否存在
		User emailOne = getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, dto.getEmail()));
		if (emailOne != null) {
			return SaResult.error("邮箱已存在");
		}

		// 密码md5加密
		password = DigestUtil.md5Hex(password);

		String codeInRedis;
		try {
			// 从redis获取缓存的邮箱验证码
			codeInRedis = redisTemplate.opsForValue().get(email);
		} catch (Exception e) {
			return SaResult.error("redis缓存读取失败");
		}
		// 验证码比对
		if (codeInRedis == null) {
			return SaResult.error("验证码错误");
		}
		if (!codeInRedis.equals(code)) {
			return SaResult.error("验证码错误");
		}

		int defaultPoint = 0;
		// 获取扫描配置
		List<Config> configList = getConfigService().list();
		for (Config con : configList) {
			if (con.getK().equals("default_point")) {
				defaultPoint = Integer.parseInt(con.getV());
			}
		}

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setPoint(defaultPoint);
		user.setCreatedTime(LocalDateTime.now());
		save(user);

		try {
			// 如果注册成功，删除redis缓存的验证码
			redisTemplate.delete(email);
		} catch (Exception e) {
			return SaResult.error("redis缓存删除失败");
		}

		return SaResult.ok("注册成功");
	}

	// 登录
	@Override
	public SaResult login(LoginDto dto, HttpServletRequest request) {
		// 判断是否登录
		if (StpUtil.isLogin()) {
			return SaResult.error("已经登录过了，<a href=\"https://app.houlangs.com/\">点此返回</a>");
		}

		// 查询用户
		User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, dto.getEmail()));
		if (user == null) {
			return SaResult.error("账号不存在");
		}

		// 判断是否封禁
		if (StpUtil.isDisable(user.getId())) {
			return SaResult.error("账号已被封禁");
		}

		// 密码比对
		String pswd = dto.getPassword();
		pswd = DigestUtil.md5Hex(pswd);
		if (pswd.equals(user.getPassword())) {
			// 更新数据库登录时间字段
			user.setLoginTime(LocalDateTime.now());
			updateById(user);

			if (dto.isRememberMe()) {
				StpUtil.login(user.getId(), new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
			} else {
				// 根据用户id,进行登录
				StpUtil.login(user.getId(), true);
			}

			// 拿到token
			String accessToken = StpUtil.getTokenInfo().tokenValue;
			// 返回用户信息
			User userData = new User();
			userData.setId(user.getId());
			userData.setEmail(user.getEmail());
			userData.setName(DesensitizedUtil.chineseName(user.getName()));
			userData.setIdNumber(DesensitizedUtil.idCardNum(user.getIdNumber(), 1, 2));
			userData.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
			userData.setRoleId(user.getRoleId());
			userData.setPoint(user.getPoint());
			userData.setStatus(user.getStatus());
			userData.setLoginTime(user.getLoginTime());
			userData.setCreatedTime(user.getCreatedTime());
			userData.setUpdatedTime(user.getUpdatedTime());

			// 包装map集合
			Map<String, Object> map = new HashMap<>();
			map.put("accessToken", accessToken);
			map.put("userData", userData);

			// 返回用户信息和token
			return SaResult.data(map);
		} else {
			return SaResult.error("密码错误");
		}
	}

	// 获取用户信息
	@Override
	public SaResult getOne() {
		long userId = StpUtil.getLoginIdAsLong();
		User user = getById(userId);
		if (user == null) {
			return SaResult.error("用户不存在");
		}
		// 返回用户信息
		User userData = new User();
		userData.setId(user.getId());
		userData.setEmail(user.getEmail());
		userData.setName(DesensitizedUtil.chineseName(user.getName()));
		userData.setIdNumber(DesensitizedUtil.idCardNum(user.getIdNumber(), 1, 2));
		userData.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
		userData.setRoleId(user.getRoleId());
		userData.setPoint(user.getPoint());
		userData.setLoginTime(user.getLoginTime());
		userData.setCreatedTime(user.getCreatedTime());
		userData.setStatus(user.getStatus());

		return SaResult.data(userData);
	}

	// 更新用户信息
	@Override
	public SaResult update(UserUpdateDto dto) {
		long userId;
		if (StpUtil.hasRole("1")) {
			userId = dto.getId();
		} else {
			userId = (long) StpUtil.getLoginId();
		}

		// 查询用户
		User user = getById(userId);
		if (user == null) {
			return SaResult.error("账号不存在");
		}

		// 更新邮箱
		// if (!StrUtil.hasBlank(dto.getEmail())) {
		// if (StpUtil.hasRole("1")) {
		// user.setEmail(dto.getEmail());
		// user.setUpdatedTime(LocalDateTime.now());
		// updateById(user);
		// } else {
		// return SaResult.error("需要管理员权限");
		// }
		// }

		// 更新密码
		if (!StrUtil.hasBlank(dto.getPassword())) {
			// 写入参数
			user.setPassword(DigestUtil.md5Hex(dto.getPassword()));
			user.setUpdatedTime(LocalDateTime.now());

			updateById(user);
		}

		// 更新用户角色
		if (dto.getRoleId() != null) {
			if (StpUtil.hasRole("1")) {
				user.setRoleId(dto.getRoleId());
				user.setUpdatedTime(LocalDateTime.now());

				updateById(user);
			} else {
				return SaResult.error("需要管理员权限");
			}
		}

		return SaResult.ok("更新成功");
	}

	// 账号封禁
	@Override
	public SaResult lock(Long id) {
		// 查询用户
		User user = getById(id);
		if (user == null) {
			return SaResult.error("账号不存在");
		}

		// 账号是否封禁
		if (StpUtil.isDisable(id)) {
			StpUtil.untieDisable(id);
			user.setStatus(1);
			user.setUpdatedTime(LocalDateTime.now());
			updateById(user);
			return SaResult.ok("解封成功");
		} else {
			// 先踢下线
			StpUtil.kickout(id);

			// 更新数据库账号封禁字段
			user.setStatus(0);
			user.setUpdatedTime(LocalDateTime.now());
			updateById(user);

			// 封禁指定账号，给予整改时间一天，86400 s = 1 day
			//改为永久封禁
			StpUtil.disable(id, -1);

			// 发送邮件通知
			// 读取信息
			String host = getConfigService().getById("mail_host").getV();
			Integer port = Integer.valueOf(getConfigService().getById("mail_port").getV());
			String from = getConfigService().getById("mail_from").getV();
			String userAccount = getConfigService().getById("mail_user").getV();
			String pass = getConfigService().getById("mail_pass").getV();
			boolean sslEnable = Boolean.parseBoolean(getConfigService().getById("mail_sslEnable").getV());
			boolean starttlsEnable = Boolean.parseBoolean(getConfigService().getById("mail_starttlsEnable").getV());
			long timeout = Long.parseLong(getConfigService().getById("mail_timeout").getV());
			long connectionTimeout = Long.parseLong(getConfigService().getById("mail_connectionTimeout").getV());
			String email = user.getEmail();

			// 配置信息
			MailAccount account = new MailAccount();
			account.setHost(host);
			account.setPort(port);
			account.setAuth(true);
			account.setFrom(from);
			account.setUser(userAccount);
			account.setPass(pass);
			account.setSslEnable(sslEnable);
			account.setTimeout(timeout);
			account.setConnectionTimeout(connectionTimeout);
			account.setStarttlsEnable(starttlsEnable);

			try {
				// 邮件发送验证码
				MailUtil.send(account, CollUtil.newArrayList(email), "厚浪域名", "<p>尊敬的用户：</p>" +
						"我们很抱歉地通知您，由于您违反了<a href=\"https://forum.houlangs.com/d/2-hou-lang-zheng-ce-lie-biao\">厚浪相关使用政策</a>，我们封禁了您的账户，并删除了账户内的全部解析。"
						+
						" 为给您带来不便表示歉意，我们深感抱歉。若你认为封禁您的账号有待商榷，请回复此邮件。我们将尽最大努力保障您的权益，并在尽可能短的时间内解决这一问题。感谢您的耐心等待和理解。",
						false);
			} catch (Exception e) {
				// 发送失败，删除缓存
				redisTemplate.delete(email);
				return SaResult.error("邮件发送失败");
			}

			return SaResult.ok("封禁告知邮件发送成功");
		}
	}

	// 注销账号
	@Override
	public SaResult delete(Long id) {
		long userId;
		if (StpUtil.hasRole("1")) {
			userId = id;

		} else {
			userId = (long) StpUtil.getLoginId();
		}

		// 查询
		User user = getById(userId);
		if (user == null) {
			return SaResult.error("用户不存在");
		}

		// 先删除域名记录（DNS和数据库）再删除用户
		DomainRecord domainRecord = getDomainRecordService()
				.getOne(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getUserId, userId));
		if (domainRecord == null) {
			removeById(userId);
		} else {
			Long domainRecordId = domainRecord.getId();
			getDomainRecordService().delete(domainRecordId);
			removeById(userId);
		}

		return SaResult.ok("注销成功");
	}

	// 用户列表
	@Override
	public SaResult list(int page, int pageSize) {
		// 构造分页构造器
		Page<User> userPage = new Page<>(page, pageSize);

		// 按创建时间排序
		page(userPage, Wrappers.<User>lambdaQuery().orderByDesc(User::getCreatedTime));

		return SaResult.data(userPage);
	}

	// 退出登录
	@Override
	public SaResult logout() {
		// 判断是否登录
		if (!StpUtil.isLogin()) {
			return SaResult.error("需要登录后操作");
		}

		StpUtil.logout();

		return SaResult.ok("成功退出登录");
	}
	
	// 签到
	@Override
	public SaResult signIn() {
		List<Config> configList = getConfigService().list();
		int signPoint = 0;
		for (Config con : configList) {
			if (con.getK().equals("sign_point")) {
				signPoint = Integer.parseInt(con.getV());
			}
		}

		PointRecord pointRecord = new PointRecord();
		User user = getById(StpUtil.getLoginIdAsLong());
		int oldPoint = user.getPoint();

		LocalDate today = LocalDate.now();
		LocalDate signedDate = getById(StpUtil.getLoginIdAsLong()).getSignIn();
		if (signedDate == null || today.isAfter(signedDate)) {
			user.setId(StpUtil.getLoginIdAsLong());
			user.setPoint(oldPoint + signPoint);
			user.setSignIn(today);
			pointRecord.setUserId(StpUtil.getLoginIdAsLong());
			pointRecord.setAction("增加");
			pointRecord.setBalance(signPoint);
			pointRecord.setRest(oldPoint + signPoint);
			pointRecord.setRemark("签到奖励" + signPoint +"积分");
			pointRecord.setCreatedTime(LocalDateTime.now());
			updateById(user);
			getPointRecordService().add(pointRecord);
		} else {
			return SaResult.error("已经签到过了，请明天再来");
		}
		return SaResult.ok("签到成功，获得1积分");
	}
}
