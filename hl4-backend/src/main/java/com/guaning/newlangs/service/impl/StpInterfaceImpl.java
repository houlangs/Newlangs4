package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guaning.newlangs.entity.Role;
import com.guaning.newlangs.entity.User;
import com.guaning.newlangs.service.RoleService;
import com.guaning.newlangs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
	private final UserService userService;
	
	public StpInterfaceImpl(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public List<String> getPermissionList(Object loginId, String s) {
		return null;
	}
	
	@Override
	public List<String> getRoleList(Object loginId, String s) {
		Long id = Long.valueOf(String.valueOf(loginId));
		User user = userService.getById(id);
		List<String> list = new ArrayList<>();
		list.add(String.valueOf(user.getRoleId()));
		return list;
	}
}
