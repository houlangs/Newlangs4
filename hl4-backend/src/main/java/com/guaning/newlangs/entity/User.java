package com.guaning.newlangs.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	
	@TableField("email")
	private String email;
	
	@TableField("password")
	private String password;
	
	@TableField("name")
	private String name;
	
	@TableField("id_number")
	private String idNumber;
	
	@TableField("phone")
	private String phone;
	
	@TableField("point")
	private Integer point;
	
	@TableField("role_id")
	private Integer roleId;
	
	/**
     * 最近一次登录时间
     */
    @TableField("login_time")
    private LocalDateTime loginTime;
	
	/**
     * 创建时间
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;
	
	/**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private LocalDateTime updatedTime;
	
	/**
	 * 账号状态
	 *      1 正常
	 *      0 封禁
	 */
	@TableField(value = "status")
	private Integer status;
	
	/**
	 * 是否删除
	 *      1 已删除
	 *      0 正常
	 */
	@TableField("is_deleted")
	@TableLogic
	private Integer isDeleted;
	
	/**
	 * 签到日期
	 */
	@TableField("sign_in")
	private LocalDate signIn;
	
	/**
	 * 是否兑换
	 */
	@TableField("is_exchange")
	private Integer isExchange;
}
