package com.guaning.newlangs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("config")
public class Config implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId("k")
	private String K;
	
	@TableField("v")
	private String v;
}
