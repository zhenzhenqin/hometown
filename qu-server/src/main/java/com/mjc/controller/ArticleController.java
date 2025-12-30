package com.mjc.controller;

import com.mjc.Result.PageResult;
import com.mjc.Result.Result;
import com.mjc.annotation.AutoLog;
import com.mjc.context.BaseContext;
import com.mjc.entity.Article;
import com.mjc.queryParam.ArticleQueryParam;
import com.mjc.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "文章/公告接口")
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 分页查询 (前后台通用)
     */
    @GetMapping
    @Operation(summary = "文章分页查询")
    public Result<PageResult> getList(ArticleQueryParam param) {
        PageResult page = articleService.getArticleList(param);
        return Result.success(page);
    }

    /**
     * 获取详情 (点击查看全文)
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情")
    public Result<Article> getDetail(@PathVariable Integer id) {
        Article article = articleService.getDetail(id);
        return Result.success(article);
    }

    /**
     * 发布文章 (仅管理员)
     */
    @AutoLog("发布文章")
    @PostMapping
    @Operation(summary = "发布文章")
    public Result publish(@RequestBody Article article) {
        // 这里可以从 Context 或 Session 中获取当前登录的 adminId 设置进去
        article.setAdminId(BaseContext.getCurrentId().intValue());
        articleService.publish(article);
        return Result.success();
    }

    /**
     * 修改文章
     */
    @AutoLog("修改文章")
    @PutMapping
    @Operation(summary = "修改文章")
    public Result update(@RequestBody Article article) {
        articleService.updateArticle(article);
        return Result.success();
    }

    /**
     * 删除文章
     */
    @AutoLog("删除文章")
    @DeleteMapping("/{ids}")
    @Operation(summary = "批量删除文章")
    public Result delete(@PathVariable List<Integer> ids) {
        articleService.delete(ids);
        return Result.success();
    }
}