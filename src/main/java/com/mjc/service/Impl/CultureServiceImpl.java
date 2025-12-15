package com.mjc.service.Impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import com.mjc.bean.PageResult;
import com.mjc.mapper.CultureMapper;
import com.mjc.service.CultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mjc.utils.RedisConstants.CULTURE_QUERY_KEY;

@Service
public class CultureServiceImpl implements CultureService {

    @Autowired
    private CultureMapper cultureMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //缓存key
    private final static String key = CULTURE_QUERY_KEY;

    /**
     * 获取文化列表
     * @param cultureQueryParam
     * @return
     */
    @Override
    public PageResult getCultureLists(CultureQueryParam cultureQueryParam) {
        if(cultureQueryParam.getPage() == null){
            cultureQueryParam.setPage(1);
        }
        if(cultureQueryParam.getPageSize() == null){
            cultureQueryParam.setPageSize(10);
        }

        //配置分页参数，及第几页和每页展示条数
        PageHelper.startPage(cultureQueryParam.getPage(), cultureQueryParam.getPageSize());

        //查询
        List<Culture> cultureList = cultureMapper.getCultureLists(cultureQueryParam);
        Page<Culture> page = (Page<Culture>) cultureList; //将查询的文化列表强转为Page对象
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 添加文化
     * @param culture
     */
    @Override
    public void insertCulture(Culture culture) {
        culture.setCreateTime(LocalDateTime.now());
        culture.setUpdateTime(LocalDateTime.now());
        cultureMapper.insertCulture(culture);
        clearCultureCache();
    }

    /**
     * 根据id获取文化
     * @param id
     * @return
     */
    @Override
    public Culture getCulture(Integer id) {
        //设置单个缓存key
        String keyId = key + id;

        //先根据缓存查询
        String cultureJson = stringRedisTemplate.opsForValue().get(keyId);

        if (cultureJson != null) {
            return JSONUtil.toBean(cultureJson, Culture.class);
        }

        //缓存未命中查询数据库
        Culture dbCulture = cultureMapper.getById(id);

        //重构缓存
        stringRedisTemplate.opsForValue().set(keyId, JSONUtil.toJsonStr(dbCulture), Duration.ofMinutes(30));

        return dbCulture;
    }

    /*
     * 修改文化
     */
    @Override
    public void updateCulture(Culture culture) {
        culture.setUpdateTime(LocalDateTime.now());
        cultureMapper.updateCulture(culture);

        //删除单个更新的文化的缓存
        String keyId = key + culture.getId();
        stringRedisTemplate.delete(keyId);

        clearCultureCache();
    }

    /**
     * 根据id批量删除文化
     * @param ids
     */
    @Override
    public void deleteCulture(List<Integer> ids) {
        cultureMapper.deleteCulture(ids);

        //批量删除缓存
        List<String> keysToDelete = new ArrayList<>();
        for (Integer id : ids) {
            keysToDelete.add(key + id);
        }
        stringRedisTemplate.delete(keysToDelete);

        clearCultureCache();
    }

    /**
     * 查询所有文化
     * @return
     */
    @Override
    public List<Culture> findCulture() {
        //查询缓存
        String cultureListJson = stringRedisTemplate.opsForValue().get(key);

        if (cultureListJson != null){
            return JSONUtil.toList(cultureListJson, Culture.class);
        }

        //缓存未命中 查询数据库
        List<Culture> cultureList = cultureMapper.findCulture();

        //将数据存入缓存中
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(cultureList), Duration.ofMinutes(30));

        return cultureList;
    }

    /**
     * 清除文化相关缓存
     */
    private void clearCultureCache() {
        stringRedisTemplate.delete(key);
    }
}
