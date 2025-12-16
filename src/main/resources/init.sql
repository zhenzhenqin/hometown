CREATE DATABASE IF NOT EXISTS hometown_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;USE hometown_db;

use hometown_db;

CREATE TABLE `user` (
                        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username` varchar(50) NOT NULL COMMENT '用户名',
                        `password` varchar(100) NOT NULL COMMENT '密码',
                        `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
                        `email` varchar(50) NOT NULL COMMENT '邮箱',
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


CREATE TABLE `attraction` (
                              `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '景点ID',
                              `name` varchar(100) NOT NULL COMMENT '景点名称',
                              `location` varchar(100) NOT NULL COMMENT '景点位置',
                              `description` text NOT NULL COMMENT '景点描述',
                              `image` varchar(200) DEFAULT NULL COMMENT '景点图片',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅游景点表';


# 管理员
CREATE TABLE `admin` (
                         `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
                         `username` varchar(50) NOT NULL COMMENT '管理员用户名',
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