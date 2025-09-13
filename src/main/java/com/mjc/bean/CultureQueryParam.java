package com.mjc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CultureQueryParam {
    private String title; //文化标题
    private Integer page = 1; //当前页数
    private Integer pageSize = 10; // 每页展示页数
}
