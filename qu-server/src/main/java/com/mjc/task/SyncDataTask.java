package com.mjc.task;

import com.mjc.contant.RedisConstants;
import com.mjc.entity.Attraction;
import com.mjc.service.AttractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
}