package com.mjc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * author mjc
 */
@Builder
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
    //private Integer isLiked; //是否已经点赞  0:未点赞  1：点赞  2：差评
    private Integer liked;   //点赞数量
    private Integer disliked;   //不喜欢数量
    private LocalDateTime createTime;  //创建时间
    private LocalDateTime updateTime;  //更新时间
}
