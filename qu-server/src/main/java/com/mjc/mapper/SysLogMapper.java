package com.mjc.mapper;

import com.mjc.entity.SysLog;
import com.mjc.queryParam.LogQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogMapper {
    /**
     * 插入日志数据
     * @param sysLog
     */
    @Insert("insert into sys_log (username, operation, method, params, ip, create_time) " +
            "values (#{username}, #{operation}, #{method}, #{params}, #{ip}, #{createTime})")
    void insert(SysLog sysLog);


    /**
     * 分页查询操作日志
     * @param logQueryParam
     * @return
     */
    List<SysLog> getLogList(LogQueryParam logQueryParam);
}
