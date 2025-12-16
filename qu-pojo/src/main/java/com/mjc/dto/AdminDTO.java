package com.mjc.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminDTO {
    private Integer id; // 主键
    private String username; // 用户名
    private String realName; // 真实姓名
    private Integer status; // 状态
    private String email; // 邮箱
    private String phone; // 手机号
    private String introduction; // 个性签名
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
