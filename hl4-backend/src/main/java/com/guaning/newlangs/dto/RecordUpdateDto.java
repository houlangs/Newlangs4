package com.guaning.newlangs.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RecordUpdateDto implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String content;
	
	private String name;
	
	private String proxied;
	
	private String type;
	
	private String comment;
	
	private String ttl;
}
