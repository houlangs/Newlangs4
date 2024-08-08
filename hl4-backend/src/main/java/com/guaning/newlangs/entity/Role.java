package com.guaning.newlangs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("role")
public class Role implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	
	@TableField("role_name")
	private String roleName;
}
