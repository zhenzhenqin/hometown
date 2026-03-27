package com.mjc.mapper;

import com.mjc.entity.AiKnowledgeBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI知识库Mapper
 */
@Mapper
public interface AiKnowledgeBaseMapper {

    /**
     * 查询所有知识库内容
     */
    @Select("SELECT * FROM ai_knowledge_base")
    List<AiKnowledgeBase> findAll();

    /**
     * 根据分类查询知识库
     */
    @Select("SELECT * FROM ai_knowledge_base WHERE category = #{category}")
    List<AiKnowledgeBase> findByCategory(String category);

    /**
     * 根据关键词搜索知识库
     */
    @Select("SELECT * FROM ai_knowledge_base WHERE title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%') OR tags LIKE CONCAT('%', #{keyword}, '%')")
    List<AiKnowledgeBase> searchByKeyword(String keyword);
}
