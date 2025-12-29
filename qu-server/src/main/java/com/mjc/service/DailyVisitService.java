package com.mjc.service;

import com.mjc.vo.VisitStatsVO;

public interface DailyVisitService {
    //统计网站访问数量
    VisitStatsVO getVisitStats();
}
