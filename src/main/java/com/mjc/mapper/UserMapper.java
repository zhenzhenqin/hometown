package com.mjc.mapper;

import com.mjc.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * 查询用户
     * @return
     */
    @Select("select * from user")
    User getUser();

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    User getUserById(Integer id);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);
}
