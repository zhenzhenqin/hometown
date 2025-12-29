package com.mjc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日访问数据实体类
 * 对应表: sys_daily_visit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyVisit {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 统计日期 (例如: 2025-12-29)
     */
    private LocalDate date;

    /**
     * 浏览量 (Page View)
     */
    private Integer pvCount;

    /**
     * 独立访客数 (Unique Visitor)
     */
    private Integer uvCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}