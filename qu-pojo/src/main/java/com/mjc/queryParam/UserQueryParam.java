package com.mjc.queryParam;

import lombok.Data;

@Data
public class UserQueryParam {
    private String username; //用户名
    private Integer status; //用户状态
    private String phone;
    private Integer page = 1;
    private Integer pageSize = 10;
}
