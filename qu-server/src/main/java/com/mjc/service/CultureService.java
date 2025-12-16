package com.mjc.service;

import com.mjc.Result.PageResult;
import com.mjc.queryParam.CultureQueryParam;
import com.mjc.entity.Culture;

import java.util.List;

public interface CultureService {

    /**
     * 分页查询文化
     * @param cultureQueryParam
     * @return
     */
    PageResult getCultureLists(CultureQueryParam cultureQueryParam);

    /**
     * 新增文化
     * @param culture
     */
    void insertCulture(Culture culture);

    /**
     * 根据id获取文化
     * @param id
     * @return
     */
    Culture getCulture(Integer id);

    /**
     * 修改文化
     * @param culture
     */
    void updateCulture(Culture culture);

    /*
    * 根据id批量删除文化
     */
    void deleteCulture(List<Integer> ids);

    /**
     * 查询所有文化
     * @return
     */
    List<Culture> findCulture();
}
