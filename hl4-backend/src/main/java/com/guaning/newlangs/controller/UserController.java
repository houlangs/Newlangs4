package com.guaning.newlangs.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.guaning.newlangs.config.RequestLimit;
import com.guaning.newlangs.dto.LoginDto;
import com.guaning.newlangs.dto.RegisterDto;
import com.guaning.newlangs.dto.UserUpdateDto;
import com.guaning.newlangs.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	//邮件验证码
	@GetMapping("/email_code")
	public SaResult emailCode(@RequestParam(value = "email") String email) {
		if (StrUtil.hasBlank(email) || !Validator.isEmail(email)) {
			return SaResult.error("参数错误");
		}
		
		return userService.emailCode(email);
	}
	
	//手机验证码
	@GetMapping("/phone_code")
	@RequestLimit(maxCount = 1, second = 60)
	public SaResult phoneCode(@RequestParam(value = "phone") String phone) {
		if (StrUtil.hasBlank(phone) || !Validator.isMobile(phone)) {
			return SaResult.error("参数错误");
		}
		
		return userService.phoneCode(phone);
	}
	
	//校验手机号码
	@GetMapping("/check_phone")
	public SaResult checkPhone(@RequestParam(value = "phone") String phone, @RequestParam(value = "code") String code) {
		if (StrUtil.hasBlank(phone) || !Validator.isMobile(phone)) {
			return SaResult.error("手机号码不正确");
		} else if (StrUtil.hasBlank(code)) {
			return SaResult.error("验证码错误");
		}
		
		return userService.checkPhone(phone, code);
	}
	
	//实名认证
	@PostMapping("/certification")
	public SaResult certification(@RequestBody JSONObject object) throws IOException {
		if (StrUtil.hasBlank(object.getString("idNumber")) || !IdcardUtil.isValidCard(object.getString("idNumber"))) {
			return SaResult.error("身份证号码不正确");
		} else if (StrUtil.hasBlank(object.getString("name"))) {
			return SaResult.error("真实姓名错误");
		}
		
		return userService.certification(object.getString("name"), object.getString("idNumber"));
	}
	
	//用户注册
	@PostMapping("/register")
	public SaResult register(@RequestBody @NotNull RegisterDto dto) {
		if (StrUtil.hasBlank(dto.getEmail())
				|| StrUtil.hasBlank(dto.getPassword())
				|| StrUtil.hasBlank(dto.getCode())) {
			return SaResult.error("参数错误");
		}
		
		if (!Validator.isEmail(dto.getEmail())) {
			return SaResult.error("请输入正确的邮箱");
		}
		
		return userService.register(dto);
	}
	
	//用户登录
	@PostMapping("/login")
	public SaResult login(@RequestBody @NotNull LoginDto dto, HttpServletRequest request) {
		if (StrUtil.hasBlank(dto.getEmail())
				|| StrUtil.hasBlank(dto.getPassword())) {
			return SaResult.error("参数错误");
		}
		
		if (!Validator.isEmail(dto.getEmail())) {
			return SaResult.error("请输入正确的邮箱");
		}
		
		return userService.login(dto, request);
	}
	
	//获取用户信息
	@GetMapping("/get_user")
	public SaResult getOne() {
		return userService.getOne();
	}
	
	//更新用户信息
	@PutMapping("/update")
	public SaResult update(@RequestBody @NotNull UserUpdateDto dto) {
		if (dto.getId() == null) {
			return SaResult.error("参数错误");
		} else if (dto.getRoleId() == null) {
			return SaResult.error("角色不能为空");
		}
		
		return userService.update(dto);
	}
	
	//账号封禁
	@GetMapping("/lock")
	public SaResult lock(@RequestParam(value = "id") Long id) {
		if (id == null) {
			return SaResult.error("参数错误");
		}
		
		return userService.lock(id);
	}
	
	//注销账号
	@DeleteMapping("/delete/{id}")
	public SaResult delete(@PathVariable("id") Long id) {
		if (id == null) {
			return SaResult.error("参数错误");
		}
		
		return userService.delete(id);
	}
	
	//用户列表
	@GetMapping("/list")
	public SaResult list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
	                     @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		return userService.list(page, pageSize);
	}
	
	//退出登录
	@GetMapping("/logout")
	public SaResult logout() {
		return userService.logout();
	}
	
	//每日签到
	@GetMapping("/sign_in")
	public SaResult signIn() {
		return userService.signIn();
	}
}
