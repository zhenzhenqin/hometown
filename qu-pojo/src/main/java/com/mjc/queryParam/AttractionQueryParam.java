package com.mjc.queryParam;

import lombok.Data;

import java.math.BigDecimal;

/**
 * author mjc
 */
@Data
public class AttractionQueryParam {
    private String name; //景点名称
    private String location; //景点位置
    private BigDecimal score; //景点评分
    private Integer page = 1;
    private Integer pageSize = 10;
}
