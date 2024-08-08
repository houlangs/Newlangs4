package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.entity.Config;
import com.guaning.newlangs.entity.PointRecord;
import com.guaning.newlangs.entity.User;
import com.guaning.newlangs.mapper.PointRecordMapper;
import com.guaning.newlangs.service.ConfigService;
import com.guaning.newlangs.service.PointRecordService;
import com.guaning.newlangs.service.UserService;
import com.guaning.newlangs.util.GenSerial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PointRecordServiceImpl extends ServiceImpl<PointRecordMapper, PointRecord> implements PointRecordService {
	final ConfigurableListableBeanFactory beanFactory;
	private final UserService userService;
	
	@Resource
	private RedisTemplate<String, Integer> redisTemplate;
	
	public PointRecordServiceImpl(ConfigurableListableBeanFactory beanFactory, UserService userService) {
		this.beanFactory = beanFactory;
		this.userService = userService;
	}
	
	public ConfigService getConfigService() {
		return beanFactory.getBean(ConfigService.class);
	}
	
	//积分记录列表
	@Override
	public SaResult list(int page, int pageSize) {
		//构造分页构造器
		Page<PointRecord> recordPage = new Page<>(page, pageSize);
		
		if (StpUtil.hasRole("1")) {
			page(recordPage, Wrappers.<PointRecord>lambdaQuery().orderByDesc(PointRecord::getCreatedTime));
		} else {
			page(recordPage, Wrappers.<PointRecord>lambdaQuery().eq(PointRecord::getUserId, StpUtil.getLoginIdAsLong()).orderByDesc(PointRecord::getCreatedTime));
		}
		
		return SaResult.data(recordPage);
	}
	
	//添加积分记录
	@Override
	public SaResult add(PointRecord dto) {
		save(dto);
		
		return SaResult.ok("积分记录添加成功");
	}
	
	//修改积分
	@Override
	public SaResult edit(PointRecord dto) {
		Long userId = dto.getUserId();
		String action = dto.getAction();
		Integer balance = dto.getBalance();
		int rest;
		String remark = dto.getRemark();
		
		Integer oldPoint = userService.getById(userId).getPoint();
		if (action.equals("增加")) {
			rest = oldPoint + balance;
		} else if (action.equals("扣除")){
			rest = oldPoint - balance;
			if (rest < 0) {
				return SaResult.error("超出积分扣除上限，请重新填写");
			}
		} else {
			return SaResult.error("Action参数有误");
		}
		
		PointRecord pointRecord = new PointRecord();
		pointRecord.setUserId(userId);
		pointRecord.setAction(action);
		pointRecord.setBalance(balance);
		pointRecord.setRest(rest);
		pointRecord.setRemark(remark);
		pointRecord.setCreatedTime(LocalDateTime.now());
		save(pointRecord);
		
		User user = new User();
		user.setId(userId);
		user.setPoint(rest);
		user.setUpdatedTime(LocalDateTime.now());
		userService.updateById(user);
		
		return SaResult.ok("修改积分成功");
	}
	
	//生成兑换码
	@Override
	public SaResult createPointCode(int point) {
		//生成兑换码
		String pointCode = GenSerial.createCode();
		
		//兑换码5分钟有效期
		try {
			//兑换码缓存到redis中，并设置有效期5分钟
			redisTemplate.opsForValue().set(pointCode, point, 5, TimeUnit.MINUTES);
		} catch (Exception e) {
			return SaResult.error("redis缓存失败");
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("pointCode", pointCode);
		map.put("langsPoint", point);
		
		return SaResult.data(map);
	}
	
	//兑换积分
	@Override
	public SaResult exchangePoint(String pointCode) {
		Long userId = StpUtil.getLoginIdAsLong();
		int commonPoint = 0;
		
		List<Config> configList = getConfigService().list();
		for (Config con : configList) {
			if (con.getK().equals("common_point")) {
				commonPoint = Integer.parseInt(con.getV());
			}
		}
		
		//通用兑换码
		if (pointCode.equals("我爱厚浪")) {
			PointRecord pointRecord = new PointRecord();
			User user = userService.getById(userId);
			int oldPoint = user.getPoint();
			
			if (user.getIsExchange() == 1) {
				return SaResult.error("您已经兑换过了，请等待下一次机会");
			} else {
				pointRecord.setUserId(userId);
				pointRecord.setAction("增加");
				pointRecord.setBalance(commonPoint);
				pointRecord.setRest(oldPoint + commonPoint);
				pointRecord.setRemark("兑换通用积分");
				pointRecord.setCreatedTime(LocalDateTime.now());
				add(pointRecord);
				
				user.setId(userId);
				user.setPoint(oldPoint + commonPoint);
				user.setIsExchange(1);
				userService.updateById(user);
				
				return SaResult.ok("兑换成功");
			}
		} else {
			//购买兑换码
			//从redis获取兑换码对应的积分
			Integer point = redisTemplate.opsForValue().get(pointCode);
			if (point == null) {
				return SaResult.error("兑换码错误");
			} else {
				PointRecord pointRecord = new PointRecord();
				User user = userService.getById(userId);
				int oldPoint = user.getPoint();
				
				pointRecord.setUserId(userId);
				pointRecord.setAction("增加");
				pointRecord.setBalance(point);
				pointRecord.setRest(oldPoint + point);
				pointRecord.setCreatedTime(LocalDateTime.now());
				add(pointRecord);
				
				user.setId(userId);
				user.setPoint(oldPoint + point);
				userService.updateById(user);
				
				//兑换成功删除redis缓存
				redisTemplate.delete(pointCode);
				
				return SaResult.ok("兑换成功");
			}
		}
	}
}
