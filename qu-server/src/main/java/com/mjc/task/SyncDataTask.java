package com.mjc.task;

import com.mjc.contant.RedisConstants;
import com.mjc.entity.Attraction;
import com.mjc.entity.DailyVisit;
import com.mjc.mapper.DailyVisitMapper;
import com.mjc.service.AttractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务 定时将redis中的点赞数据异步同步到数据库
 */
@Component
@Slf4j
public class SyncDataTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private DailyVisitMapper dailyVisitMapper;

    /**
     * 定时将 Redis 中的点赞数据同步到 MySQL
     * 每5s更新一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void syncLikedCountToDb() {
        log.info("开始执行定时任务：同步 Redis 点赞数到 MySQL...");

        // 1. 从 Redis 中获取所有的点赞数据
        // key 是 attractionId，Value 是点赞数
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(RedisConstants.ATTRACTION_LIKED_KEY);
        Map<Object, Object> map2 = stringRedisTemplate.opsForHash().entries(RedisConstants.ATTRACTION_DISLIKED_KEY);

        syncData(map);
        syncData(map2);
    }

    /**
     * 同步 Redis 中的点赞数据到 MySQL
     * @param map
     */
    private void syncData(Map<Object, Object> map) {
        if (map.isEmpty()) {
            log.info("Redis 中暂无点赞数据，无需同步");
            return;
        }

        // 2. 组装需要更新的实体列表
        List<Attraction> updateList = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            try {
                Long id = Long.parseLong(entry.getKey().toString());
                Integer count = Integer.parseInt(entry.getValue().toString());

                Attraction attraction = new Attraction();
                attraction.setId(Math.toIntExact(id));
                attraction.setLiked(count);

                updateList.add(attraction);
            } catch (Exception e) {
                log.error("解析 Redis 数据异常: key={}, value={}", entry.getKey(), entry.getValue());
            }
        }

        // 3. 批量更新到数据库
        if (!updateList.isEmpty()) {
            for (Attraction attraction : updateList){
                boolean success = attractionService.updateAttraction(attraction);
                if (success) {
                    log.info("同步成功，共同步 {} 条数据", updateList.size());
                } else {
                    log.error("同步失败");
                }
            }
        }
    }


    /**
     * 任务二：同步每日网站访问量 (PV/UV)
     * 频率：每天凌晨 00:05:00 执行
     */
    @Scheduled(cron = "0 5 0 * * ?")
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void syncYesterdayVisitData() {
        log.info("定时任务开始同步昨日网站访问数据...");

        // 1. 获取昨天的日期 (例如今天29号，统计的是28号的数据)
        String yesterday = LocalDate.now().minusDays(1).toString();

        // 2. 拼接 Redis Key
        String uvKey = RedisConstants.UV_KEY_PREFIX + yesterday;
        String pvKey = RedisConstants.PV_KEY_PREFIX + yesterday;

        try {
            // 3. 从 Redis 获取数据
            String pvStr = stringRedisTemplate.opsForValue().get(pvKey);
            Long uvCount = stringRedisTemplate.opsForSet().size(uvKey);

            // 处理空值
            int pv = pvStr != null ? Integer.parseInt(pvStr) : 0;
            int uv = uvCount != null ? uvCount.intValue() : 0;

            // 4. 封装实体类
            DailyVisit dailyVisit = DailyVisit.builder()
                    .date(LocalDate.parse(yesterday))
                    .pvCount(pv)
                    .uvCount(uv)
                    .createTime(LocalDateTime.now())
                    .build();

            // 5. 写入数据库
            dailyVisitMapper.insert(dailyVisit);

            log.info("同步完成日期: {}, PV: {}, UV: {}", yesterday, pv, uv);

        } catch (Exception e) {
            log.error("同步失败昨日数据同步异常: {}", e.getMessage());
        }
    }
}