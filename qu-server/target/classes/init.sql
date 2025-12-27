CREATE DATABASE IF NOT EXISTS hometown_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;USE hometown_db;

use hometown_db;

CREATE TABLE `user` (
                        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username` varchar(50) NOT NULL COMMENT '用户名',
                        `password` varchar(100) NOT NULL COMMENT '密码',
                        `real_name` varchar(50) COMMENT '真实姓名',
                        `status`  int default 1 not null comment '状态 0:拉黑，1:正常',
                        `email` varchar(50) COMMENT '邮箱',
                        `phone` varchar(20) NOT NULL COMMENT '手机号',
                        `introduction` text COMMENT '个人简介',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `username_unique` (`username`),
                        UNIQUE KEY `email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

CREATE TABLE `culture` (
                           `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文化ID',
                           `title` varchar(100) NOT NULL COMMENT '文化标题',
                           `content` text NOT NULL COMMENT '文化内容',
                           `image` varchar(200) DEFAULT NULL COMMENT '文化图片',
                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家乡文化表';

CREATE TABLE `specialty` (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '特产ID',
                             `name` varchar(100) NOT NULL COMMENT '特产名称',
                             `description` text NOT NULL COMMENT '特产描述',
                             `price` decimal(10,2) NOT NULL COMMENT '特产价格',
                             `image` varchar(200) DEFAULT NULL COMMENT '特产图片',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家乡特产表';


create table attraction
(
    id          int auto_increment comment '景点ID'
        primary key,
    name        varchar(100)                            not null comment '景点名称',
    location    varchar(100)                            not null comment '景点位置',
    description text                                    null comment '景点描述',
    image       varchar(200)                            null comment '景点图片',
    create_time datetime      default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime      default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    score       decimal(3, 1) default 0.0               null comment '景点评分分数',
    liked       int unsigned  default '0'               null comment '点赞数量',
    disliked    int unsigned  default '0'               null comment '不喜欢数量',
    longitude   decimal(10, 6)                          null comment '经度',
    latitude    decimal(10, 6)                          null comment '纬度'
)
    comment '旅游景点表' charset = utf8mb4;


# 管理员
CREATE TABLE `admin` (
                         `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
                         `username` varchar(50) not NULL COMMENT '管理员用户名',
                         `password` varchar(100) NOT NULL COMMENT '密码',
                         `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
                         `status`  int default 1 not null comment '状态 0:禁用，1:启用',
                         `email` varchar(50) NOT NULL COMMENT '邮箱',
                         `phone` varchar(20) NOT NULL COMMENT '手机号',
                         `introduction` text COMMENT '个人简介',
                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `admin_username_unique` (`username`),
                         UNIQUE KEY `admin_email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员信息表';

ALTER TABLE `attraction`
    ADD COLUMN `liked` int unsigned DEFAULT '0' NULL COMMENT '点赞数量',
    ADD COLUMN `disliked` int unsigned DEFAULT '0' NULL COMMENT '不喜欢数量';

CREATE TABLE `sys_log` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `username` varchar(50) DEFAULT NULL COMMENT '操作人',
                           `operation` varchar(50) DEFAULT NULL COMMENT '操作描述(如: 新增用户)',
                           `method` varchar(200) DEFAULT NULL COMMENT '请求方法(类名.方法名)',
                           `params` text COMMENT '请求参数',
                           `ip` varchar(64) DEFAULT NULL COMMENT '操作IP',
                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

-- 新增执行时长字段
ALTER TABLE sys_log ADD COLUMN `time` bigint(20) DEFAULT 0 COMMENT '执行时长(毫秒)';

INSERT INTO `admin`
(`id`, `username`, `password`, `real_name`, `status`, `email`, `phone`, `introduction`, `create_time`, `update_time`)
VALUES
    (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'mjc', 1, '3378405015@qq.com', '19817193250', '这是一个大帅逼', '2025-12-16 14:37:04', '2025-12-21 03:08:59');

-- 新增经纬度
ALTER TABLE `user`
    ADD COLUMN `longitude` decimal(10, 6) DEFAULT NULL COMMENT '经度',
    ADD COLUMN `latitude` decimal(10, 6) DEFAULT NULL COMMENT '纬度';

-- 对普通用户插入ip字段以及所在城市字段
ALTER TABLE `user`
    ADD COLUMN `ip` varchar(64) DEFAULT NULL COMMENT '操作IP',
    ADD COLUMN `city` varchar(50) DEFAULT NULL COMMENT '所在城市';

-- 对普通用户插入注册时所在城市字段
ALTER TABLE `user`
    ADD COLUMN `register_city` varchar(50) DEFAULT NULL COMMENT '注册时所在城市';


-- 对管理员也插入ip字段以及所在城市字段
ALTER TABLE `admin`
    ADD COLUMN `ip` varchar(64) DEFAULT NULL COMMENT '操作IP',
    ADD COLUMN `city` varchar(50) DEFAULT NULL COMMENT '所在城市';
