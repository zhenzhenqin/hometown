package com.mjc.queryParam;

import lombok.Data;

@Data
public class LogQueryParam {
    // 当前页码 (默认1)
    private Integer page = 1;
    
    // 每页条数 (默认10)
    private Integer pageSize = 10;
    
    // 搜索条件：用户名 (可选)
    private String username;
    
    // 搜索条件：操作描述 (可选)
    private String operation;
}