package com.mjc.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.bean.Attraction;
import com.mjc.bean.AttractionQueryParam;
import com.mjc.bean.PageResult;
import com.mjc.mapper.AttractionMapper;
import com.mjc.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttractionServiceImpl implements AttractionService {

    @Autowired
    private AttractionMapper attractionMapper;

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

        PageHelper.startPage(attractionQueryParam.getPage(), attractionQueryParam.getPageSize());

        List<Attraction> list = attractionMapper.list(attractionQueryParam);

        Page p = (Page) list;
        return new PageResult(p.getTotal(), p.getResult());
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
    }

    /**
     * 根据id查询景点
     * @param id
     * @return
     */
    @Override
    public Attraction getById(Integer id) {
        return attractionMapper.getById(id);
    }

    /**
     * 修改景点
     * @param attraction
     */
    @Override
    public void updateAttraction(Attraction attraction) {
        attraction.setUpdateTime(LocalDateTime.now());
        attractionMapper.updateAttraction(attraction);
    }

    /**
     * 批量删除景点
     * @param ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        attractionMapper.deleteByIds(ids);
    }
}
