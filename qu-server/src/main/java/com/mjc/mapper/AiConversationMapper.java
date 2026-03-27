package com.mjc.mapper;

import com.mjc.entity.AiConversation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * AI会话Mapper
 */
@Mapper
public interface AiConversationMapper {

    /**
     * 新增会话
     */
    @Insert("INSERT INTO ai_conversation (session_id, user_id, title, message_count, last_message, create_time, update_time) " +
            "VALUES (#{sessionId}, #{userId}, #{title}, #{messageCount}, #{lastMessage}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AiConversation conversation);

    /**
     * 根据sessionId查询
     */
    @Select("SELECT * FROM ai_conversation WHERE session_id = #{sessionId}")
    AiConversation findBySessionId(String sessionId);

    /**
     * 更新会话信息
     */
    @Update("UPDATE ai_conversation SET message_count = #{messageCount}, last_message = #{lastMessage}, update_time = NOW() WHERE session_id = #{sessionId}")
    void updateBySessionId(AiConversation conversation);

    /**
     * 查询用户的所有会话
     */
    @Select("SELECT * FROM ai_conversation WHERE user_id = #{userId} OR user_id IS NULL ORDER BY update_time DESC")
    List<AiConversation> findByUserId(Integer userId);

    /**
     * 根据ID删除会话
     */
    @Delete("DELETE FROM ai_conversation WHERE id = #{id}")
    void deleteById(Long id);
}
