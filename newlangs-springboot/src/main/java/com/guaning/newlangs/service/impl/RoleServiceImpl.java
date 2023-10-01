package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.dto.DeleteRoleDto;
import com.guaning.newlangs.entity.Role;
import com.guaning.newlangs.entity.User;
import com.guaning.newlangs.mapper.RoleMapper;
import com.guaning.newlangs.service.RoleService;
import com.guaning.newlangs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
	private final UserService userService;

	@Autowired
	public RoleServiceImpl(UserService userService) {
		this.userService = userService;
	}

	// Add Role
	@Override
	public SaResult add(Role role) {
		// Check if the role name already exists
		Role nameOne = getOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, role.getRoleName()));
		if (nameOne != null) {
			return SaResult.error("Role name already exists");
		}
		role.setRoleName(role.getRoleName());

		save(role);

		return SaResult.ok("Added successfully");
	}

	// Edit Role
	@Override
	public SaResult edit(Role role) {
		Role one = getById(role);
		if (one == null) {
			return SaResult.error("Role does not exist");
		}

		updateById(role);

		return SaResult.ok("Edited successfully");
	}

	// Get Role List
	@Override
	public SaResult list(int page, int pageSize) {
		// Create a pagination builder
		Page<Role> rolePage = new Page<>(page, pageSize);

		// Sort by creation time
		page(rolePage, Wrappers.<Role>lambdaQuery().orderByAsc(Role::getId));

		return SaResult.data(rolePage);
	}

	// Delete Role
	@Override
	public SaResult delete(DeleteRoleDto dto) {
		Integer id = dto.getId();
		Integer newRoleId = dto.getNewRoleId();
		// Check if the role exists
		Role role = getById(id);
		if (role == null) {
			return SaResult.error("Data does not exist");
		}

		long count = userService.count(Wrappers.<User>lambdaQuery().eq(User::getRoleId, id));
		if (count == 0 || newRoleId == null) {
			// Delete directly
			removeById(id);
		} else {
			// First update the user's new role ID
			userService.update(Wrappers.<User>lambdaUpdate().eq(User::getRoleId, id).set(User::getRoleId, newRoleId));
			// Then delete the role
			removeById(id);
		}

		return SaResult.ok("Deleted successfully");
	}

	// Get the Number of Users Belonging to a Role
	@Override
	public SaResult getCount(Integer id) {
		long count = userService.count(Wrappers.<User>lambdaQuery().eq(User::getRoleId, id));

		return SaResult.data(count);
	}
}
