package com.mjc.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String phone;
    private String code; // 验证码结果
    private String uuid; // 验证码唯一标识
}
