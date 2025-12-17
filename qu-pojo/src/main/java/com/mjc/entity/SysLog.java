package com.mjc.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysLog {
    private Long id;
    private String username; //操作人
    private String operation; //操作描述
    private String method;  //请求方法
    private String params;  //请求参数
    private String ip;      //操作ip
    private Long time;      //执行时长
    private LocalDateTime createTime;  //操作时间
}
