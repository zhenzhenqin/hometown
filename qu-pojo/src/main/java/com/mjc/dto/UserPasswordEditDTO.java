package com.mjc.dto;

import lombok.Data;

@Data
public class UserPasswordEditDTO {
    //管理员id
    private Long userId;

    //旧密码
    private String oldPassword;

    //新密码
    private String newPassword;
}
