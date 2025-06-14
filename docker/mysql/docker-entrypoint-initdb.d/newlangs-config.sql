SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 配置表
CREATE TABLE IF NOT EXISTS `config` (
  `k` varchar(255) NOT NULL COMMENT 'key',
  `v` varchar(255) DEFAULT NULL COMMENT 'value',
  PRIMARY KEY (`k`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `config` VALUES
('common_code',NULL),
('common_point','100'),
('default_point','200'),
('mail_connectionTimeout','0'),
('mail_from',NULL),
('mail_host',NULL),
('mail_pass',NULL),
('mail_port',NULL),
('mail_sslEnable','true'),
('mail_starttlsEnable','true'),
('mail_timeout','0'),
('mail_user',NULL),
('no_prefix','app'),
('scan_keyWords','厚浪'),
('sign_point','1');

-- 域名表
CREATE TABLE IF NOT EXISTS `domain` (
  `id` bigint(36) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `domain_id` varchar(50) DEFAULT NULL COMMENT '域名商提供的domain_id',
  `dns` varchar(36) DEFAULT NULL COMMENT 'dns',
  `name` varchar(50) DEFAULT NULL COMMENT '域名名称',
  `role_ids` varchar(255) DEFAULT NULL COMMENT '该域分配的角色',
  `point` int(11) DEFAULT NULL COMMENT '域名价格',
  `comment` varchar(255) DEFAULT NULL COMMENT '域名介绍',
  `created_time` datetime DEFAULT NULL COMMENT '域名创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '域名更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DNS配置表
CREATE TABLE IF NOT EXISTS `domain_config` (
  `dns` varchar(36) NOT NULL COMMENT 'dns平台',
  `config` varchar(1024) DEFAULT NULL COMMENT 'key密钥信息',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dns`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 域名记录表
CREATE TABLE IF NOT EXISTS `domain_record` (
  `id` bigint(36) NOT NULL COMMENT '主键',
  `user_id` bigint(36) DEFAULT NULL COMMENT '用户id',
  `did` bigint(50) DEFAULT NULL COMMENT 'domain表的id',
  `record_id` varchar(50) DEFAULT NULL COMMENT '域名服务商提供的记录ID',
  `prefix` varchar(50) DEFAULT NULL COMMENT '用户解析的域名前缀',
  `type` varchar(36) DEFAULT NULL COMMENT '解析类型',
  `value` varchar(255) DEFAULT NULL COMMENT '解析地址',
  `line_id` int(11) DEFAULT NULL COMMENT '线路编号',
  `line` varchar(255) DEFAULT NULL COMMENT '线路名称',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `ttl` int(11) DEFAULT NULL COMMENT 'TTL',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `checked_time` datetime DEFAULT NULL COMMENT '脚本检查违规时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 积分记录表
CREATE TABLE IF NOT EXISTS `point_record` (
  `id` bigint(36) NOT NULL COMMENT '主键',
  `user_id` bigint(36) DEFAULT NULL COMMENT '用户id',
  `action` varchar(36) DEFAULT NULL COMMENT '收支',
  `balance` int(11) DEFAULT NULL COMMENT '收支积分',
  `rest` int(11) DEFAULT NULL COMMENT '剩余积分',
  `remark` varchar(255) DEFAULT NULL COMMENT '收支去向备注',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(36) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(36) NOT NULL COMMENT '主键',
  `email` varchar(36) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(36) DEFAULT NULL COMMENT '密码',
  `name` varchar(36) DEFAULT NULL COMMENT '真实姓名',
  `id_number` char(18) DEFAULT NULL COMMENT '身份证号',
  `phone` char(11) DEFAULT NULL COMMENT '手机号码',
  `point` int(11) DEFAULT '100' COMMENT '积分',
  `role_id` int(11) DEFAULT '2' COMMENT '用户组',
  `login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `created_time` datetime DEFAULT NULL COMMENT '注册时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更改时间',
  `status` tinyint(11) DEFAULT '1' COMMENT '账号状态',
  `is_deleted` tinyint(11) DEFAULT '0' COMMENT '删除账号',
  `sign_in` date DEFAULT NULL COMMENT '签到日期',
  `is_exchange` tinyint(11) DEFAULT '0' COMMENT '是否兑换',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入默认管理员用户
INSERT INTO `user` VALUES (1,'admin@houlangs.com','e10adc3949ba59abbe56e057f20f883e','厚浪云','810000200001010639','13900000000',100,1,NULL,"2024-01-01 00:00:00",NULL,1,0,NULL,0);

SET FOREIGN_KEY_CHECKS = 1;