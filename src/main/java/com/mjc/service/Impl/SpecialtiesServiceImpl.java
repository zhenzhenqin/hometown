package com.mjc.service.Impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.entity.PageResult;
import com.mjc.entity.Specialties;
import com.mjc.entity.SpecialtiesQueryParam;
import com.mjc.mapper.SpecialtiesMapper;
import com.mjc.service.SpecialtiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mjc.utils.RedisConstants.SPECIALTY_QUERY_KEY;

@Service
public class SpecialtiesServiceImpl implements SpecialtiesService {

    @Autowired
    private SpecialtiesMapper specialtiesMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static String key = SPECIALTY_QUERY_KEY;

    /**
     * 对特产进行条件分页查询
     * @param specialtiesQueryParam
     * @return
     */
    @Override
    public PageResult list(SpecialtiesQueryParam specialtiesQueryParam) {

        //如果参数为空 则赋默认值 防止抛出空指针异常
        if(specialtiesQueryParam.getPage() == null){
            specialtiesQueryParam.setPage(1);
        }
        if(specialtiesQueryParam.getPageSize() == null){
            specialtiesQueryParam.setPageSize(10);
        }
        //配置分页参数，及第几页和每页展示条数
        PageHelper.startPage(specialtiesQueryParam.getPage(), specialtiesQueryParam.getPageSize());
        //开始分页
        List<Specialties> specialtiesList = specialtiesMapper.list(specialtiesQueryParam);
        Page<Specialties> page = (Page<Specialties>) specialtiesList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增特产
     * @param specialties
     */
    @Override
    public void add(Specialties specialties) {
        specialties.setCreateTime(LocalDateTime.now());
        specialties.setUpdateTime(LocalDateTime.now());
        specialtiesMapper.add(specialties);
        clearSpecialtiesCache();
    }

    /**
     * 根据id查询特产
     * @param id
     * @return
     */
    @Override
    public Specialties getById(Integer id) {
        String keyId = key + id;

        String specialtiesJson = stringRedisTemplate.opsForValue().get(keyId);

        if (specialtiesJson != null){
            return JSONUtil.toBean(specialtiesJson, Specialties.class);
        }

        Specialties specialties = specialtiesMapper.getById(id);

        stringRedisTemplate.opsForValue().set(keyId, JSONUtil.toJsonStr(specialties), Duration.ofMinutes(30));

        return specialties;
    }

    /**
     * 修改特产
     * @param specialties
     */
    @Override
    public void update(Specialties specialties) {
        specialties.setUpdateTime(LocalDateTime.now());
        specialtiesMapper.update(specialties);

        //只需要删除改动的缓存
        String keyId = key + specialties.getId();

        stringRedisTemplate.delete(keyId);

        clearSpecialtiesCache();
    }

    /**
     * 根据id批量删除特产
     * @param ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        specialtiesMapper.deleteByIds(ids);

        //批量删除缓存
        List<String> keysToDelete = new ArrayList<>();
        for (Integer id : ids) {
            keysToDelete.add(key + id);
        }

        stringRedisTemplate.delete(keysToDelete);

        clearSpecialtiesCache();
    }

    /**
     * 查询所有特产
     * @return
     */
    @Override
    public List<Specialties> findSpecialties() {

        String specialtiesJson = stringRedisTemplate.opsForValue().get(key);

        if (specialtiesJson != null) {
            return JSONUtil.toList(specialtiesJson, Specialties.class);
        }

        List<Specialties> specialtiesList = specialtiesMapper.findSpecialties();

        //将数据存入缓存中
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(specialtiesList), Duration.ofMinutes(30));

        return specialtiesList;
    }

    /**
     * 清除特产相关缓存
     */
    private void clearSpecialtiesCache() {
        stringRedisTemplate.delete(key);
    }
}
