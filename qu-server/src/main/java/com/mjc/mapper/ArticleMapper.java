package com.mjc.mapper;

import com.mjc.entity.Article;
import com.mjc.queryParam.ArticleQueryParam;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ArticleMapper {
    // 分页查询列表
    List<Article> getArticleLists(ArticleQueryParam param);

    // 新增
    void insertArticle(Article article);

    // 根据ID查询
    Article getById(Integer id);

    // 更新
    void updateArticle(Article article);

    // 删除
    void deleteArticle(List<Integer> ids);
}