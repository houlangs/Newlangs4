package com.guaning.newlangs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guaning.newlangs.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
