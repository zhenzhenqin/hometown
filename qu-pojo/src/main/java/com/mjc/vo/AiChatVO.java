package com.mjc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI问答响应VO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiChatVO {
    private String sessionId;     // 会话ID
    private String answer;        // AI回答
    private String title;         // 会话标题（第一条消息时返回）
    private Integer tokens;       // 本次消耗token
    private List<AiMessageVO> history; // 历史消息
    private LocalDateTime createTime; // 创建时间
    
    /**
     * 消息VO
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AiMessageVO {
        private Long id;
        private String role;      // user / assistant
        private String content;
        private LocalDateTime createTime;
    }
}
