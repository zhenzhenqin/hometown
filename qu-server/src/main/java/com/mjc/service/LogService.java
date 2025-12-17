package com.mjc.service;

import com.mjc.Result.PageResult;
import com.mjc.queryParam.LogQueryParam;

public interface LogService {
    /**
     * 分页查询操作日志
     * @param logQueryParam
     * @return
     */
    PageResult getLogList(LogQueryParam logQueryParam);
}
