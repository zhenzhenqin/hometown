package com.mjc.queryParam;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleQueryParam {
    private String title;     // 按标题搜索
    private Integer adminId;  // 作者ID
    private Integer page = 1; 
    private Integer pageSize = 10;
}