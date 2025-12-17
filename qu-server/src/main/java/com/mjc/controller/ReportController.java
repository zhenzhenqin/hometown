package com.mjc.controller;


import com.mjc.Result.Result;
import com.mjc.service.ReportService;
import com.mjc.vo.DashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
