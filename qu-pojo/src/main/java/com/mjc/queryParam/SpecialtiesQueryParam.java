package com.mjc.queryParam;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpecialtiesQueryParam {
    private String name;
    private BigDecimal price;
    private Integer page = 1;
    private Integer pageSize = 10;
}
