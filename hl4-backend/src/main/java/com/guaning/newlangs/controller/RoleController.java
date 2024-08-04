package com.guaning.newlangs.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.guaning.newlangs.dto.DeleteRoleDto;
import com.guaning.newlangs.entity.Role;
import com.guaning.newlangs.service.RoleService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {
	private RoleService roleService;
	
	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	//新增角色
	@PostMapping("/add")
	public SaResult add(@RequestBody @NotNull Role role) {if (StrUtil.hasBlank(role.getRoleName())) {
			return SaResult.error("角色名称不能为空");
		}
		
		return roleService.add(role);
	}
	
	//编辑角色
	@PostMapping("/edit")
	public SaResult edit(@RequestBody @NotNull Role role) {
		if (role.getId() == null) {
			return SaResult.error("未获取到有效参数");
		} else if (StrUtil.hasBlank(role.getRoleName())) {
			return SaResult.error("角色名称不能为空");
		}
		
		return roleService.edit(role);
	}
	
	//角色列表
	@GetMapping("/list")
	public SaResult list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
	                         @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		return roleService.list(page, pageSize);
	}
	
	//删除角色
	@PostMapping("/delete")
	public SaResult delete(@RequestBody DeleteRoleDto dto) {
		if (dto.getId() == null) {
			return SaResult.error("未获取到有效参数");
		}
		
		return roleService.delete(dto);
	}
	
	//获取属于某用户角色数量
	@GetMapping("/get_count")
	public SaResult getCount(@RequestParam Integer id) {
		if (id == null) {
			return SaResult.error("未获取到有效参数");
		}
		
		return roleService.getCount(id);
	}
}
