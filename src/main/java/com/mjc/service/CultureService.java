package com.mjc.service;

import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import com.mjc.bean.PageResult;

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
}
