package com.mjc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegionVO {
    /**
     * 地区名称 (例如: "杭州市", "宁波市")
     * 对应前端地图的 areaName
     */
    private String name;

    /**
     * 该地区的用户数量
     */
    private Integer value;
}