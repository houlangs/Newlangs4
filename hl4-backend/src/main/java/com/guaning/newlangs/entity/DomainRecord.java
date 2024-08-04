package com.guaning.newlangs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("domain_record")
public class DomainRecord implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	
	@TableField("user_id")
	private Long userId;
	
	@TableField("did")
	private Long did;
	
	@TableField("record_id")
	private String recordId;
	
	@TableField("prefix")
	private String prefix;
	
	@TableField("type")
	private String type;
	
	@TableField("value")
	private String value;
	
	@TableField("line_id")
	private Integer lineId;
	
	@TableField("line")
	private String line;
	
	@TableField("comment")
	private String comment;
	
	@TableField("ttl")
	private Integer ttl;
	
	@TableField("created_time")
	private LocalDateTime createdTime;
	
	@TableField("updated_time")
	private LocalDateTime updatedTime;
}
