package com.guaning.newlangs.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeleteRoleDto implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer newRoleId;
}
