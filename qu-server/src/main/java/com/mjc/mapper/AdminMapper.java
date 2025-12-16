package com.mjc.mapper;

import com.mjc.entity.Admin;
import com.mjc.queryParam.AdminQueryParam;
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
     * @param id
     * @return
     */
    @Select("select * from admin where id = #{id}")
    Admin getAdmin(Long id);

    /**
     * 查询所有管理员信息
     * @param adminQueryParam
     * @return
     */
    List<Admin> queryAllAdmin(AdminQueryParam adminQueryParam);
}
