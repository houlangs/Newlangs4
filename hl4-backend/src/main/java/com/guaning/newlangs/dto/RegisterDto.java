package com.guaning.newlangs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RegisterDto implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	private String email;
	
	private String password;
	
	private String code;
}
