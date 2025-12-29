package com.mjc.service.Impl;

import com.mjc.entity.DailyVisit;
import com.mjc.mapper.DailyVisitMapper;
import com.mjc.service.DailyVisitService;
import com.mjc.vo.VisitStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mjc.contant.RedisConstants.PV_KEY_PREFIX;
import static com.mjc.contant.RedisConstants.UV_KEY_PREFIX;

@Service
public class DailyVisitServiceImpl implements DailyVisitService {

    @Autowired
    private DailyVisitMapper dailyVisitMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public VisitStatsVO getVisitStats() {
        // 1. 从数据库查询过去 6 天的历史数据 (不包含今天)
        // 假设 mapper.selectLast7Days() 返回的是按日期倒序的，我们需要反转或者按日期正序查
        List<DailyVisit> historyList = dailyVisitMapper.selectLast7Days();
        // 如果数据库查出来是倒序(最新在前面)，建议反转一下变成正序(旧->新)
        Collections.reverse(historyList);

        // 2. 准备结果容器
        List<String> dateList = new ArrayList<>();
        List<Integer> pvList = new ArrayList<>();
        List<Integer> uvList = new ArrayList<>();

        // 3. 填充历史数据
        for (DailyVisit visit : historyList) {
            dateList.add(visit.getDate().toString());
            pvList.add(visit.getPvCount());
            uvList.add(visit.getUvCount());
        }

        // 4. 获取并填充 "今天" 的实时数据 (从 Redis)
        String today = LocalDate.now().toString();

        // 查 Redis
        String todayPvStr = redisTemplate.opsForValue().get(PV_KEY_PREFIX + today);
        Long todayUvCount = redisTemplate.opsForSet().size(UV_KEY_PREFIX + today);

        int todayPv = todayPvStr != null ? Integer.parseInt(todayPvStr) : 0;
        int todayUv = todayUvCount != null ? todayUvCount.intValue() : 0;

        // 添加到列表末尾
        dateList.add(today);
        pvList.add(todayPv);
        uvList.add(todayUv);

        // 5. 封装返回
        return VisitStatsVO.builder()
                .dateList(dateList)
                .pvList(pvList)
                .uvList(uvList)
                .build();
    }
}