package com.mjc.service.Impl;

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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mjc.utils.RedisConstants.ATTRACTION_QUERY_KEY;


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
        String attraction = stringRedisTemplate.opsForValue().get(keyId);

        if (attraction != null) {
            return JSONUtil.toBean(attraction, Attraction.class);
        }

        //缓存未命中查询数据库
        Attraction dbattraction = attractionMapper.getById(id);

        //重构缓存
        stringRedisTemplate.opsForValue().set(keyId, JSONUtil.toJsonStr(dbattraction), Duration.ofMinutes(30));

        return dbattraction;
    }

    /**
     * 修改景点
     * @param attraction
     */
    @Override
    public void updateAttraction(Attraction attraction) {
        //修改的景点的idkey
        String keyId = key + attraction.getId();

        attraction.setUpdateTime(LocalDateTime.now());

        attractionMapper.updateAttraction(attraction);

        //删除单个更新的景点的缓存
        stringRedisTemplate.delete(keyId);

        clearAttractionCache();
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
    public List<Attraction> findAttraction() {
        //查询缓存
        String attractionList = stringRedisTemplate.opsForValue().get(key);

        if (attractionList != null){
            return JSONUtil.toList(attractionList, Attraction.class);
        }

        //缓存未命中 查询数据库
        List<Attraction> list = attractionMapper.findAttraction();

        //将数据存入缓存中
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(list), Duration.ofMinutes(30));

        return list;
    }

    /**
     * 清除景点相关缓存
     */
    private void clearAttractionCache() {
        stringRedisTemplate.delete(key);
    }
}
