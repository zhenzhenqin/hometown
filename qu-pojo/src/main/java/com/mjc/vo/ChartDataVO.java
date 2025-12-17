package com.mjc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataVO {
    /**
     * 数据名称 (x轴或饼图的图例)
     * 例如：景点名称、地区名称
     */
    private String name;

    /**
     * 数据值 (y轴或饼图的占比)
     * 例如：点赞数、差评数、统计数量
     */
    private Object value; 
}