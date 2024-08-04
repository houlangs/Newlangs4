package com.guaning.newlangs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserUpdateDto implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	//private String username;
	//
	//private String email;
	//
	//private String code;
	//
	//private String phone;
	
	private String password;
	
	//private Integer point;
	
	private Integer roleId;
}
