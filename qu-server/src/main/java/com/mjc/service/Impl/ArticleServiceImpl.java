package com.mjc.service.Impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.Result.PageResult;
import com.mjc.entity.Article;
import com.mjc.mapper.ArticleMapper;
import com.mjc.queryParam.ArticleQueryParam;
import com.mjc.service.ArticleService; // 记得创建这个接口
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CACHE_KEY = "article:detail:";

    @Override
    public PageResult getArticleList(ArticleQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Article> list = articleMapper.getArticleLists(param);
        Page<Article> page = (Page<Article>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void publish(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        // 默认值
        if(article.getIsPublic() == null) article.setIsPublic(1);

        articleMapper.insertArticle(article);
    }

    @Override
    public Article getDetail(Integer id) {
        // 1. 查 Redis
        String json = redisTemplate.opsForValue().get(CACHE_KEY + id);
        if (json != null) {
            return JSONUtil.toBean(json, Article.class);
        }

        // 2. 查 DB
        Article article = articleMapper.getById(id);

        // 3. 写入 Redis (30分钟过期)
        if (article != null) {
            redisTemplate.opsForValue().set(CACHE_KEY + id, JSONUtil.toJsonStr(article), Duration.ofMinutes(30));
        }
        return article;
    }

    @Override
    public void updateArticle(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.updateArticle(article);
        // 删除缓存，保证数据一致性
        redisTemplate.delete(CACHE_KEY + article.getId());
    }

    @Override
    public void delete(List<Integer> ids) {
        articleMapper.deleteArticle(ids);
        // 循环删除缓存
        ids.forEach(id -> redisTemplate.delete(CACHE_KEY + id));
    }
}