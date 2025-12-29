package com.mjc.mapper;

import com.mjc.entity.DailyVisit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DailyVisitMapper {
    /**
     * 插入每日统计数据
     * 注意：如果日期重复(已存在)，则不做处理 (IGNORE) 或者报错
     */
    @Insert("INSERT INTO sys_daily_visit (date, pv_count, uv_count, create_time) " +
            "VALUES (#{date}, #{pvCount}, #{uvCount}, #{createTime})")
    void insert(DailyVisit dailyVisit);

    /**
     * 查询最近7天的数据 (用于前端图表展示)
     * 按日期升序排列
     */
    @Select("SELECT * FROM sys_daily_visit ORDER BY date DESC LIMIT 7")
    List<DailyVisit> selectLast7Days();
}
