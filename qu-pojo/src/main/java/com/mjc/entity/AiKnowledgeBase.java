package com.mjc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI知识库实体类
 * 存储景点/特产/文化的介绍，用于AI回答的上下文
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiKnowledgeBase {
    private Integer id;           // 主键ID
    private String category;      // 分类：attraction-景点, specialty-特产, culture-文化
    private Integer sourceId;     // 关联来源ID
    private String title;        // 标题
    private String content;      // 详细内容
    private String tags;         // 标签
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间
}
