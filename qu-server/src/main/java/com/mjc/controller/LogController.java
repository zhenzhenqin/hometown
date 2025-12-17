package com.mjc.controller;

import com.github.pagehelper.Page;
import com.mjc.Result.PageResult;
import com.mjc.Result.Result;
import com.mjc.entity.SysLog;
import com.mjc.queryParam.LogQueryParam;
import com.mjc.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "系统日志接口")
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询操作日志
     * @param logQueryParam
     * @return
     */
    @Operation(summary = "分页查询操作日志")
    @GetMapping("/list")
    public Result<PageResult> getLogList(LogQueryParam logQueryParam) {
        PageResult result = logService.getLogList(logQueryParam);
        return Result.success(result);
    }
}
