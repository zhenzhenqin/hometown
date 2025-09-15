package com.mjc.service;

import com.mjc.bean.Attraction;
import com.mjc.bean.AttractionQueryParam;
import com.mjc.bean.PageResult;

import java.util.List;

public interface AttractionService {

    /**
     * 分页查询景点
     * @param attractionQueryParam
     * @return
     */
    PageResult list(AttractionQueryParam attractionQueryParam);

    /**
     * 添加景点
     * @param attraction
     */
    void addAttraction(Attraction attraction);

    /**
     * 根据id查询景点
     * @param id
     * @return
     */
    Attraction getById(Integer id);

    /*
     * 修改景点
     * @param attraction
     */
    void updateAttraction(Attraction attraction);

    /**
     * 批量删除景点
     * @param ids
     */
    void deleteByIds(List<Integer> ids);

    /*
     * 查询所有景点
     */
    List<Attraction> findAttraction();
}
