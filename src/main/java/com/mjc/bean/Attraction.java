package com.mjc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attraction {
    private Integer id; //主键
    private String name; //景点名称
    private String location; //景点位置
    private String description; //景点描述
    private String image;  //图片
    private BigDecimal score;  //评分
    private LocalDateTime createTime;  //创建时间
    private LocalDateTime updateTime;  //更新时间
}
