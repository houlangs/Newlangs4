package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.dto.DeleteRoleDto;
import com.guaning.newlangs.entity.Role;

public interface RoleService extends IService<Role> {
	//添加角色
	SaResult add(Role role);
	
	//编辑角色
	SaResult edit(Role role);
	
	//获取角色列表
	SaResult list(int page, int pageSize);
	
	//删除角色
	SaResult delete(DeleteRoleDto dto);
	
	//获取属于某角色用户数量
	SaResult getCount(Integer id);
}
