package com.mjc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AI问答请求DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiChatDTO {
    private String sessionId;      // 会话ID（可选，新会话不传）
    private String question;       // 用户问题
    private String cityName;       // 城市名称，如"衢州"、"台州"
}
