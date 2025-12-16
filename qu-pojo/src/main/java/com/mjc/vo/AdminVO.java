package com.mjc.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminVO {
    private Integer id; // 主键
    private String username; // 用户名
    private String password; // 密码
    private String token; //jwt
}
