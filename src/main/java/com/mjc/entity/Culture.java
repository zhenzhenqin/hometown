package com.mjc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * author mjc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Culture {
    private Integer id; // id
    private String title; //标题
    private String content; //内容
    private String image; //图片
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime; //更改时间
}
