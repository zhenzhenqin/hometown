package com.mjc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//个人信息

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id; // 主键
    private String username; // 用户名
    private String password; // 密码
    private String realName; // 真实姓名
    private String email; // 邮箱
    private String phone; // 手机号
    private String introduction; // 个性签名
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
