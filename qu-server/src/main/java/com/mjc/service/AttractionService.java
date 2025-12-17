package com.mjc.service;

import com.mjc.Result.PageResult;
import com.mjc.queryParam.AttractionQueryParam;
import com.mjc.entity.Attraction;

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

    /**
     * 点赞功能
     * @param id
     * @return
     */
    Boolean liked(Integer id);

    /**
     * 取消点赞功能
     * @param id
     * @return
     */
    boolean noLiked(Integer id);

    /**
     * 差评
     * @param id
     * @return
     */
    boolean disLiked(Integer id);

    /**
     * 取消差评
     * @param id
     * @return
     */
    boolean noDisLiked(Integer id);
}
