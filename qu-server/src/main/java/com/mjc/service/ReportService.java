package com.mjc.service;

import com.mjc.vo.ChartDataVO;
import com.mjc.vo.DashboardVO;

import java.util.List;

public interface ReportService {

    /**
     * 获取后台首页驾驶舱统计数据
     * @return
     */
    DashboardVO getIndexDashboard();

    /**
     * 图表：景点人气 Top5 (点赞数)
     * @return
     */
    List<ChartDataVO> getTopPopular();

    /**
     * 图表：用户注册趋势 (折线图)
     * @return
     */
    List<ChartDataVO> getUserGrowthTrend();

    /**
     * "图表：用户状态分布 (饼图)
     * @return
     */
    List<ChartDataVO> getUserStatusDistribution();

    /**
     * 图表：特产价格区间分布 (饼图/直方图)
     * @return
     */
    List<ChartDataVO> getSpecialtyPriceDistribution();
}
