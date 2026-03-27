package com.mjc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI消息实体类
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiMessage {
    private Long id;             // 消息ID
    private Long conversationId; // 会话ID
    private String role;         // 角色：user-用户, assistant-AI助手
    private String content;      // 消息内容
    private Integer tokens;      // 消耗token数
    private LocalDateTime createTime;  // 创建时间
}
