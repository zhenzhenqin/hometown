package com.mjc.mapper;

import com.mjc.entity.AiMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI消息Mapper
 */
@Mapper
public interface AiMessageMapper {

    /**
     * 新增消息
     */
    @Insert("INSERT INTO ai_message (conversation_id, role, content, tokens, create_time) " +
            "VALUES (#{conversationId}, #{role}, #{content}, #{tokens}, NOW())")
    void insert(AiMessage message);

    /**
     * 查询会话的所有消息
     */
    @Select("SELECT * FROM ai_message WHERE conversation_id = #{conversationId} ORDER BY create_time ASC")
    List<AiMessage> findByConversationId(Long conversationId);

    /**
     * 查询最近的N条消息
     */
    @Select("SELECT * FROM ai_message WHERE conversation_id = #{conversationId} ORDER BY create_time DESC LIMIT #{limit}")
    List<AiMessage> findRecentByConversationId(Long conversationId, int limit);
}
