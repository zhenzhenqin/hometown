package com.mjc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


/**
 * 此处用于发表我的emo小文章
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Integer id;
    private Integer adminId;      // 发布管理员ID
    private String adminName;      // 发布管理员名称
    private String title;         // 标题
    private String summary;       // 摘要
    private String contentMd;     // Markdown 原文
    private String coverImg;      // 封面图
    private Integer viewCount;    // 阅读量
    private Integer likeCount;    // 点赞量
    private Integer isPublic;     // 1:公开 0:私密
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}