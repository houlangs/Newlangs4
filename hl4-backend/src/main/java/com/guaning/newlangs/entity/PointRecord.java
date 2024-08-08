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
import java.time.LocalDateTime;

@Data
@TableName("point_record")
public class PointRecord implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	
	@TableField("user_id")
	private Long userId;
	
	@TableField("action")
	private String action;
	
	@TableField("balance")
	private Integer balance;
	
	@TableField("rest")
	private Integer rest;
	
	@TableField("remark")
	private String remark;
	
	@TableField("created_time")
	private LocalDateTime createdTime;
}
