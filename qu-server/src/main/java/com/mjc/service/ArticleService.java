package com.mjc.service;

import com.mjc.Result.PageResult;
import com.mjc.entity.Article;
import com.mjc.queryParam.ArticleQueryParam;

import java.util.List;

public interface ArticleService {
    /**
     * 分页参数查询
     * @param param
     * @return
     */
    PageResult getArticleList(ArticleQueryParam param);

    /**
     * 获取文章详细内容
     * @param id
     * @return
     */
    Article getDetail(Integer id);

    /**
     * 上传文章
     * @param article
     */
    void publish(Article article);

    /**
     * 更新文章
     * @param article
     */
    void updateArticle(Article article);

    /**
     * 删除文章
     * @param ids
     */
    void delete(List<Integer> ids);
}
