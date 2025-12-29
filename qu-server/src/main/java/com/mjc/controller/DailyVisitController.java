package com.mjc.controller;

import com.mjc.Result.Result;
import com.mjc.service.DailyVisitService;
import com.mjc.vo.VisitStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "统计网站用户数量接口")
@RequestMapping("/dailyVisit")
public class DailyVisitController {

    @Autowired
    private DailyVisitService dailyVisitService;

    /**
     * 获取最近7天的网站访问数据 (混合了MySQL历史数据 + Redis今日实时数据)
     * @return 包含日期、PV、UV数组的VO对象
     */
    @GetMapping("/stats")
    @Operation(summary = "获取最近7天访问统计数据")
    public Result<VisitStatsVO> getVisitStats() {
        log.info("开始获取网站访问统计数据...");
        VisitStatsVO statsVO = dailyVisitService.getVisitStats();
        return Result.success(statsVO);
    }

    @GetMapping("/ping")
    public Result<String> ping() {
        // 这个接口不需要写任何业务逻辑
        // 它的唯一作用就是让请求成功到达后端，从而触发拦截器的 preHandle 方法
        return Result.success("pong");
    }
}