package com.mjc.mapper;

import com.mjc.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {
    /**
     * 登录
     * @param username
     * @return
     */
    @Select("select * from admin where username = #{username}")
    Admin getByUserName(String username);

    /**
     * 根据id查询管理员
     * @param id
     * @return
     */
    @Select("select * from admin where id = #{id}")
    Admin getById(Integer id);

    /**
     * 更新管理员
     * @param admin
     */
    void updateAdmin(Admin admin);

    /**
     * 查询所有管理员
     * @return
     */
    @Select("select * from admin")
    List<Admin> findAllAdmin();

    /**
     * 根据ThreadLocal存储id获取用户欣喜
     * @param id
     * @return
     */
    @Select("select * from admin where id = #{id}")
    Admin getAdmin(Long id);
}
