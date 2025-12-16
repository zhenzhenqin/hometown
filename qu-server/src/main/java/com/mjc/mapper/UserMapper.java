package com.mjc.mapper;

import com.mjc.annotation.AutoFill;
import com.mjc.entity.User;
import com.mjc.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @AutoFill(value = OperationType.UPDATE)
    void updateUser(User user);

    /**
     * 查询用户
     * @return
     */
    @Select("select * from user")
    List<User> findUser();
}
