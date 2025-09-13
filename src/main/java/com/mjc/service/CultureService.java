package com.mjc.service;

import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import com.mjc.bean.PageResult;

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
}
