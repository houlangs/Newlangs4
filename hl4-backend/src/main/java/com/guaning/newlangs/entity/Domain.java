package com.guaning.newlangs.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("domain")
public class Domain implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	@TableField("domain_id")
	private String domainId;
	
	@TableField("dns")
	private String dns;
	
	@TableField("name")
	private String name;
	
	@TableField("role_ids")
	private String roleIds;
	
	@TableField("point")
	private Integer point;
	
	@TableField("comment")
	private String comment;
	
	@TableField("created_time")
	private LocalDateTime createdTime;
	
	@TableField("updated_time")
	private LocalDateTime updatedTime;
}
