package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.dto.DeleteRoleDto;
import com.guaning.newlangs.entity.Role;

public interface RoleService extends IService<Role> {
    // Add Role
    SaResult add(Role role);

    // Edit Role
    SaResult edit(Role role);

    // Get Role List
    SaResult list(int page, int pageSize);

    // Delete Role
    SaResult delete(DeleteRoleDto dto);

    // Get the Number of Users Belonging to a Role
    SaResult getCount(Integer id);
}
