package com.mjc.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.mjc.Result.PageResult;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.queryParam.AttractionQueryParam;
import com.mjc.entity.Attraction;
import com.mjc.mapper.AttractionMapper;
import com.mjc.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mjc.contant.RedisConstants.ATTRACTION_QUERY_KEY;


@Service
public class AttractionServiceImpl implements AttractionService {

    @Autowired
    private AttractionMapper attractionMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //缓存key
    private final static String key = ATTRACTION_QUERY_KEY;

    /**
     * 分页查询景点
     * @param attractionQueryParam
     * @return
     */
    @Override
    public PageResult list(AttractionQueryParam attractionQueryParam) {
        if (attractionQueryParam.getPage() == null) {
            attractionQueryParam.setPage(1);
        }
        if (attractionQueryParam.getPageSize() == null) {
            attractionQueryParam.setPageSize(10);
        }

        /*//缓存key
        String attrKey = ATTRACTION_QUERY_KEY;

        // 尝试从缓存获取
        String cachedResult = stringRedisTemplate.opsForValue().get(attrKey);
        if (cachedResult != null) {
            // 反序列化缓存数据
            return JSONUtil.toBean(cachedResult, PageResult.class);
        }*/

        PageHelper.startPage(attractionQueryParam.getPage(), attractionQueryParam.getPageSize());

        List<Attraction> list = attractionMapper.list(attractionQueryParam);

        //计算评分
        for (Attraction attraction : list){
            BigDecimal score = calculateScore(attraction);
            attraction.setScore(score);
        }

        Page p = (Page) list;

        PageResult pageResult = new PageResult(p.getTotal(), p.getResult());

        // 存入缓存，设置过期时间（例如30分钟）
        /*stringRedisTemplate.opsForValue().set(
                attrKey,
                JSONUtil.toJsonStr(pageResult),
                Duration.ofMinutes(30)
        );*/

        return pageResult;
    }

    /**
     * 添加景点
     * @param attraction
     */
    @Override
    public void addAttraction(Attraction attraction) {
        attraction.setCreateTime(LocalDateTime.now());
        attraction.setUpdateTime(LocalDateTime.now());
        attractionMapper.addAttraction(attraction);
        clearAttractionCache();
    }

    /**
     * 根据id查询景点
     * @param id
     * @return
     */
    @Override
    public Attraction getById(Integer id) {
        //设置单个缓存key
        String keyId = key + id;

        //先根据缓存查询
        String json = stringRedisTemplate.opsForValue().get(keyId);

        //缓存命中
        if (StrUtil.isNotBlank(json)) {
            // 如果查询到的为空，则返回null
            if ("".equals(json)) {
                return null;
            }
            //返回数据
            return JSONUtil.toBean(json, Attraction.class);
        }

        //缓存未命中查询数据库
        Attraction dbattraction = attractionMapper.getById(id);

        //处理数据库结果
        if (dbattraction == null) {
            //解决缓存穿透：将空值存入缓存，设置过期时间 此处设置为5分钟
            stringRedisTemplate.opsForValue().set(keyId, "", 5, TimeUnit.MINUTES);
            return null;
        }

        dbattraction.setScore(calculateScore(dbattraction));

        //重构缓存
        stringRedisTemplate.opsForValue().set(keyId, JSONUtil.toJsonStr(dbattraction), Duration.ofMinutes(30));

        return dbattraction;
    }

    /**
     * 修改景点
     *
     * @param attraction
     * @return
     */
    @Override
    public boolean updateAttraction(Attraction attraction) {
        //修改的景点的idkey
        String keyId = key + attraction.getId();

        attraction.setUpdateTime(LocalDateTime.now());

        attractionMapper.updateAttraction(attraction);

        //删除单个更新的景点的缓存
        stringRedisTemplate.delete(keyId);

        clearAttractionCache();
        return false;
    }

    /**
     * 批量删除景点
     * @param ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        attractionMapper.deleteByIds(ids);

        //批量删除缓存
        List<String> keysToDelete = new ArrayList<>();
        for (Integer id : ids) {
            keysToDelete.add(key + id);
        }

        stringRedisTemplate.delete(keysToDelete);

        clearAttractionCache();
    }

    /**
     * 查询所有景点
     * @return
     */
    @Override
    public List<Attraction> findAttraction(Long userId) {
        // 1. 原有的查询逻辑  先查询缓存 缓存没有再查询数据库
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s != null) {
            return JSONUtil.toList(s, Attraction.class);
        }

        //2. 缓存为空 直接查询数据库
        List<Attraction> list = attractionMapper.findAttraction();

        //计算评分
        for (Attraction attraction : list){
            BigDecimal score = calculateScore(attraction);
            attraction.setScore(score);
        }

        // 2. 如果用户已登录，遍历列表检查 Redis 状态
        if (userId != null && list != null && !list.isEmpty()) {
            for (Attraction attr : list) {
                checkUserStatus(attr, userId);
            }
        }

        return list;
    }

    /**
     * 点赞相关功能
     * @param id
     * @return
     */
    @Override
    public Boolean liked(Integer id) {
        Attraction attraction = attractionMapper.getById(id);

        if (attraction != null) {
            attraction.setLiked(attraction.getLiked() + 1);
            //更新评分
            BigDecimal score = calculateScore(attraction);
            attraction.setScore(score);
            attractionMapper.updateAttraction(attraction);
            clearAttractionCache();
            return true;
        }

        return false;
    }

    /**
     * 取消点赞功能
     * @param id
     * @return
     */
    @Override
    public boolean noLiked(Integer id) {
        Attraction attraction = attractionMapper.getById(id);

        if (attraction != null) {
            attraction.setLiked(attraction.getLiked() - 1);
            //更新评分
            BigDecimal score = calculateScore(attraction);
            attraction.setScore(score);
            attractionMapper.updateAttraction(attraction);
            clearAttractionCache();
            return true;
        }

        return false;
    }

    /**
     * 差评
     * @param id
     * @return
     */
    @Override
    public boolean disLiked(Integer id) {
        Attraction attraction = attractionMapper.getById(id);
        if (attraction != null) {
            int currentDisliked = attraction.getDisliked() == null ? 0 : attraction.getDisliked();
            attraction.setDisliked(currentDisliked + 1);
            //更新评分
            BigDecimal score = calculateScore(attraction);
            attraction.setScore(score);
            attractionMapper.updateAttraction(attraction);
            clearAttractionCache();
            return true;
        }
        return false;
    }

    /**
     * 取消差评
     * @param id
     * @return
     */
    @Override
    public boolean noDisLiked(Integer id) {
        Attraction attraction = attractionMapper.getById(id);
        if (attraction != null) {
            int currentDisliked = attraction.getDisliked() == null ? 0 : attraction.getDisliked();
            // 防止变成负数
            attraction.setDisliked(Math.max(0, currentDisliked - 1));
            BigDecimal score = calculateScore(attraction);
            attraction.setScore(score);
            attractionMapper.updateAttraction(attraction);
            clearAttractionCache();
            return true;
        }
        return false;
    }

    /**
     * 清除景点相关缓存
     */
    private void clearAttractionCache() {
        stringRedisTemplate.delete(key);
    }


    /**
     * 辅助方法：检查单个景点的用户状态
     */
    private void checkUserStatus(Attraction attr, Long userId) {
        String userIdStr = userId.toString();

        // 1. 构造 Redis Key
        // 假设你的常量类里有这些定义
        String likeKey = com.mjc.contant.RedisConstants.ATTRACTION_LIKED_KEY + attr.getId();
        String dislikeKey = com.mjc.contant.RedisConstants.ATTRACTION_DISLIKED_KEY + attr.getId();

        // 2. 检查是否点赞
        Boolean isMemberLiked = stringRedisTemplate.opsForSet().isMember(likeKey, userIdStr);
        if (Boolean.TRUE.equals(isMemberLiked)) {
            attr.setIsLiked(1); // 设置为已点赞
            return; // 互斥的，如果是赞了就不可能是踩，直接返回
        }

        // 3. 检查是否差评
        Boolean isMemberDisliked = stringRedisTemplate.opsForSet().isMember(dislikeKey, userIdStr);
        if (Boolean.TRUE.equals(isMemberDisliked)) {
            attr.setIsLiked(2); // 设置为已差评
        } else {
            attr.setIsLiked(0); // 无操作
        }
    }


    /**
     * 计算分数方法
     */
    private BigDecimal calculateScore(Attraction attraction){
        //获取景区id
        Integer id = attraction.getId();

        //根据景区id查询点赞和差评数量
        Attraction a = attractionMapper.getById(id);
        Integer likedNumber = a.getLiked();
        Integer dislikedNumber = a.getDisliked();

        BigDecimal result = BigDecimal.valueOf(0.0);

        //计算分数 喜欢/总的
        //如果两个都是0

        if ((likedNumber + dislikedNumber) == 0) {
            result = BigDecimal.valueOf(0.0);
        } else {
            result = BigDecimal.valueOf((likedNumber / (double) (likedNumber + dislikedNumber)) * 10).setScale(1, RoundingMode.HALF_UP);
        }

        return result;
    }
}
