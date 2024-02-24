package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.dto.LoginDto;
import com.guaning.newlangs.dto.RegisterDto;
import com.guaning.newlangs.dto.UserUpdateDto;
import com.guaning.newlangs.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService extends IService<User> {
	//邮箱验证码
	SaResult emailCode(String email);
	
	//手机验证码
	SaResult phoneCode(String phone);
	
	//校验手机验证码
	SaResult checkPhone(String phone, String code);
	
	//实名认证
	SaResult certification(String name, String IdNumber) throws IOException;
	
	//用户注册
	SaResult register(RegisterDto dto);
	
	//用户登录
	SaResult login(LoginDto dto, HttpServletRequest request);
	
	//获取用户信息
	SaResult getOne();
	
	//更新用户信息
	SaResult update(UserUpdateDto dto);
	
	//封禁账号
	SaResult lock(Long id);
	
	//删除账号
	SaResult delete(Long id);
	
	//用户列表
	SaResult list(int page, int pageSize);
	
	//退出登录
	SaResult logout();
	
	//用户签到
	SaResult signIn();
}
