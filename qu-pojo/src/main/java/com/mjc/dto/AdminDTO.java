package com.mjc.dto;

import lombok.Data;

@Data
public class AdminDTO {
    private String username;
    private String password;
    private String code; // 验证码结果
    private String uuid; // 验证码唯一标识
}
