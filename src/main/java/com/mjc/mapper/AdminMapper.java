package com.mjc.mapper;

import com.mjc.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    /**
     * 登录
     * @param username
     * @return
     */
    @Select("select * from admin where username = #{username}")
    Admin getByUserName(String username);
}
