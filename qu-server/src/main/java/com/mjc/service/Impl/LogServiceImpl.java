package com.mjc.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.Result.PageResult;
import com.mjc.entity.SysLog;
import com.mjc.mapper.SysLogMapper;
import com.mjc.queryParam.LogQueryParam;
import com.mjc.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 分页查询操作日志
     * @param logQueryParam
     * @return
     */
    @Override
    public PageResult getLogList(LogQueryParam logQueryParam) {
        if(logQueryParam.getPage() == null){
            logQueryParam.setPage(1);
        }
        if(logQueryParam.getPageSize() == null){
            logQueryParam.setPageSize(10);
        }

        PageHelper.startPage(logQueryParam.getPage(), logQueryParam.getPageSize());

        List<SysLog> sysLogList = sysLogMapper.getLogList(logQueryParam);

        Page<SysLog> page = (Page<SysLog>) sysLogList;

        return new PageResult(page.getTotal(), page.getResult());
    }
}
