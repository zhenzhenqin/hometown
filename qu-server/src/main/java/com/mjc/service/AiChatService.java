package com.mjc.service;

import com.mjc.dto.AiChatDTO;
import com.mjc.vo.AiChatVO;

/**
 * AI智能问答服务接口
 */
public interface AiChatService {

    /**
     * 发送消息并获取AI回复
     * @param dto 问答请求
     * @param userId 用户ID（可为null表示游客）
     * @return AI回答
     */
    AiChatVO chat(AiChatDTO dto, Integer userId);

    /**
     * 流式获取AI回答（同时保存到数据库）
     * @param question 问题
     * @param sessionId 会话ID
     * @return AI回答
     */
    String chatStream(String question, String sessionId);

    /**
     * 获取会话历史
     * @param sessionId 会话ID
     * @return 历史消息
     */
    AiChatVO getHistory(String sessionId);

    /**
     * 删除会话
     * @param sessionId 会话ID
     */
    void deleteSession(String sessionId);

    /**
     * 获取用户的所有会话
     * @param userId 用户ID
     * @return 会话列表
     */
    AiChatVO getUserSessions(Integer userId);
}
