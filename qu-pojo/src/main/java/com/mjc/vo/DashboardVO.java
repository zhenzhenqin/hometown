package com.mjc.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardVO {
    /**
     * 用户总数
     */
    private Long userCount;

    /**
     * 景点总数
     */
    private Long attractionCount;

    /**
     * 特产总数
     */
    private Long specialtyCount;

    /**
     * 文化文章数
     */
    private Long cultureCount;

    /**
     * 全站总点赞数
     */
    private Long totalLikes;
}