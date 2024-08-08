package com.guaning.newlangs.entity;

import com.alibaba.fastjson.JSON;
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
@TableName("domain_config")
public class DomainConfig implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId("dns")
	private String dns;
	
	@TableField("config")
	private String config;
	
	@TableField("created_time")
	private LocalDateTime createdTime;
	
	@TableField("updated_time")
	private LocalDateTime updatedTime;
}
