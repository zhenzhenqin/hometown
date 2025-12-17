package com.mjc.controller;


import com.mjc.Result.Result;
import com.mjc.annotation.AutoLog;
import com.mjc.service.ReportService;
import com.mjc.vo.ChartDataVO;
import com.mjc.vo.DashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "报表相关接口")
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 数据统计
     * @return
     */
    @Operation(summary = "获取后台首页驾驶舱统计数据")
    @GetMapping("/dashboard")
    public Result<DashboardVO> getDashboardMetrics() {
        log.info("Requesting dashboard metrics...");
        DashboardVO vo = reportService.getIndexDashboard();
        return Result.success(vo);
    }

    /**
     * 图表：景点人气 Top5 (点赞数)
     * @return
     */
    @Operation(summary = "图表：景点人气 Top5 (点赞数)")
    @GetMapping("/attraction/popular")
    public Result<List<ChartDataVO>> getAttractionPopular() {
        return Result.success(reportService.getTopPopular());
    }

    /**
     * 图表：用户注册趋势 (折线图)
     * @return
     */
    @Operation(summary = "图表：用户注册趋势 (折线图)")
    @GetMapping("/user/trend")
    public Result<List<ChartDataVO>> getUserGrowthTrend() {
        // 数据示例: [{name: "2023-12-01", value: 5}, {name: "2023-12-02", value: 12}...]
        return Result.success(reportService.getUserGrowthTrend());
    }

    /**
     * 图表：用户状态分布 (饼图)
     * @return
     */
    @Operation(summary = "图表：用户状态分布 (饼图)")
    @GetMapping("/user/status")
    public Result<List<ChartDataVO>> getUserStatusDistribution() {
        // 数据示例: [{name: "正常用户", value: 98}, {name: "被拉黑/禁用", value: 2}]
        return Result.success(reportService.getUserStatusDistribution());
    }

    /**
     * 图表：特产价格区间分布 (饼图/直方图)
     * @return
     */
    @Operation(summary = "图表：特产价格区间分布 (饼图/直方图)")
    @GetMapping("/specialty/price-dist")
    public Result<List<ChartDataVO>> getSpecialtyPriceDistribution() {
        // 数据示例: [{name: "0-50元", value: 15}, {name: "300元以上", value: 2}...]
        return Result.success(reportService.getSpecialtyPriceDistribution());
    }

}
