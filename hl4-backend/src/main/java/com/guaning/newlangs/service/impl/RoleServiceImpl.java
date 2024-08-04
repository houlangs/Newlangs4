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
	
	public RoleServiceImpl(UserService userService) {
		this.userService = userService;
	}
	
	//添加角色
	@Override
	public SaResult add(Role role) {
		//查询是否存在
		Role nameOne = getOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, role.getRoleName()));
		if (nameOne != null) {
			return SaResult.error("角色名称已存在");
		}
		role.setRoleName(role.getRoleName());
		
		save(role);
		
		return SaResult.ok("添加成功");
	}
	
	//编辑角色
	@Override
	public SaResult edit(Role role) {
		Role one = getById(role);
		if (one == null) {
			return SaResult.error("角色不存在");
		}
		
		updateById(role);
		
		return SaResult.ok("编辑成功");
	}
	
	//获取角色列表
	@Override
	public SaResult list(int page, int pageSize) {
		//构造分页构造器
		Page<Role> rolepage = new Page<>(page, pageSize);
		
		//按创建时间排序
		page(rolepage, Wrappers.<Role>lambdaQuery().orderByAsc(Role::getId));
		
		return SaResult.data(rolepage);
	}
	
	//删除角色
	@Override
	public SaResult delete(DeleteRoleDto dto) {
		Integer id = dto.getId();
		Integer newRoleId = dto.getNewRoleId();
		//查询
		Role role = getById(id);
		if (role == null) {
			return SaResult.error("数据不存在");
		}
		
		long count = userService.count(Wrappers.<User>lambdaQuery().eq(User::getRoleId, id));
		if (count == 0 || newRoleId == null) {
			//直接删除
			removeById(id);
		} else {
			//先更新用户新角色ID
			userService.update(Wrappers.<User>lambdaUpdate().eq(User::getRoleId, id).set(User::getRoleId, newRoleId));
			//后删除角色
			removeById(id);
		}
		
		return SaResult.ok("删除成功");
	}
	
	//获取属于某角色用户数量
	@Override
	public SaResult getCount(Integer id) {
		long count = userService.count(Wrappers.<User>lambdaQuery().eq(User::getRoleId, id));
		
		return SaResult.data(count);
	}
}
