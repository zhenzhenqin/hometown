package com.mjc.dto;

import lombok.Data;

@Data
public class AdminPasswordEditDTO {
    //管理员id
    private Long adminId;

    //旧密码
    private String oldPassword;

    //新密码
    private String newPassword;
}
