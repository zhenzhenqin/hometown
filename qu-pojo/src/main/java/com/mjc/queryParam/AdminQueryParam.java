package com.mjc.queryParam;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminQueryParam {
    private String username; //用户名
    private Integer page = 1;
    private Integer pageSize = 10;
}
