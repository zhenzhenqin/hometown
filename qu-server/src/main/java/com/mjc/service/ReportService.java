package com.mjc.service;

import com.mjc.vo.DashboardVO;

public interface ReportService {

    /**
     * 获取后台首页驾驶舱统计数据
     * @return
     */
    DashboardVO getIndexDashboard();
}
