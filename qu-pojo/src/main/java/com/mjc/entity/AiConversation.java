package com.mjc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI会话实体类
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiConversation {
    private Long id;             // 会话ID
    private Integer userId;      // 用户ID
    private String sessionId;    // 会话唯一标识
    private String title;        // 会话标题
    private Integer messageCount;// 消息数量
    private String lastMessage;  // 最后一条消息
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间
}
