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
    // Email Verification Code
    SaResult emailCode(String email);

    // Phone Verification Code
    SaResult phoneCode(String phone);

    // Verify Phone Verification Code
    SaResult checkPhone(String phone, String code);

    // Real Name Authentication
    SaResult certification(String name, String IdNumber) throws IOException;

    // User Registration
    SaResult register(RegisterDto dto);

    // User Login
    SaResult login(LoginDto dto, HttpServletRequest request);

    // Get User Information
    SaResult getOne();

    // Update User Information
    SaResult update(UserUpdateDto dto);

    // Lock Account
    SaResult lock(Long id);

    // Delete Account
    SaResult delete(Long id);

    // User List
    SaResult list(int page, int pageSize);

    // Logout
    SaResult logout();

    // User Sign-In
    SaResult signIn();
}
