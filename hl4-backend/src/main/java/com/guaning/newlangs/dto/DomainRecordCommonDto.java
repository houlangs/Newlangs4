package com.guaning.newlangs.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DomainRecordCommonDto implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long userId;
	
	private Long did;
	
	private String recordId;
	
	private String prefix;
	
	private String type;
	
	private String value;
	
	private Integer lineId;
	
	private String line;
	
	private String comment;
	
	private LocalDateTime createdTime;
	
	private LocalDateTime updatedTime;
	
	private String email;
	
	private String key;
	
	private String domainId;
}
