package com.mjc.interceptor;

import com.mjc.contant.RedisConstants;
import com.mjc.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * 网站流量统计拦截器
 * 策略：优先使用前端传来的设备ID，如果没有则降级使用IP
 */
@Component
@Slf4j
public class VisitInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 前端传来的 Header Key
    private static final String HEADER_DEVICE_ID = "X-Device-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        //System.out.println(">>> 流量拦截器触发: " + request.getRequestURI());
        String headerId = request.getHeader(HEADER_DEVICE_ID);
        //System.out.println(">>> Header ID: " + headerId);

        // 获取当天日期
        String today = LocalDate.now().toString();

        //获取前端Header 唯一设备id
        String visitorId = request.getHeader(HEADER_DEVICE_ID);

        //System.out.println("获取到的 Header ID: " + visitorId);

        //如果未获取到设备唯一id 则降级使用ip
        if (visitorId == null || visitorId.isEmpty() || "null".equals(visitorId)) {
            visitorId = IpUtil.getIpAddr(request);
            log.debug("未获取到设备ID，降级使用IP统计: {}", visitorId);
        }

        //记录 PV (Page View) -> String 自增
        String pvKey = RedisConstants.PV_KEY_PREFIX + today;
        redisTemplate.opsForValue().increment(pvKey);

        //这里的 visitorId 可能是 "UUID" 也可能是 "IP"，Set 会自动处理去重
        String uvKey = RedisConstants.UV_KEY_PREFIX + today;
        redisTemplate.opsForSet().add(uvKey, visitorId);

        // 设置过期时间 (保留3天，历史数据由定时任务持久化到MySQL)
        // 只有 Key 不存在时才设置，避免每次请求都设置
        if (Boolean.FALSE.equals(redisTemplate.hasKey(pvKey))) {
            redisTemplate.expire(pvKey, 3, TimeUnit.DAYS);
            redisTemplate.expire(uvKey, 3, TimeUnit.DAYS);
        }

        return true;
    }
}
