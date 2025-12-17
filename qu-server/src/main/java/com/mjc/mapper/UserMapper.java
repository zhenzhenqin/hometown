package com.mjc.mapper;

import com.mjc.annotation.AutoFill;
import com.mjc.entity.User;
import com.mjc.enumeration.OperationType;
import com.mjc.queryParam.UserQueryParam;
import com.mjc.vo.ChartDataVO;
import org.apache.ibatis.annotations.Insert;
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

    /**
     * 登录接口
     * @param username
     * @return
     */
    @Select("select  * from user where username = #{username}")
    User getUserByUsername(String username);

    /**
     * 条件分页查询
     * @param userQueryParam
     * @return
     */
    List<User> queryAllUser(UserQueryParam userQueryParam);

    /**
     * 用户注册功能
     * @param user
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into user (username, password, phone, status) values (#{username}, #{password}, #{phone}, #{status})")
    void registerUser(User user);

    /**
     * 统计用户数量
     * @return
     */
    @Select("select count(id) from user")
    Long countAll();

    /**
     * 图表：用户注册趋势 (折线图)
     * @return
     */
    @Select("select DATE(create_time) as name, count(id) as value from user group by DATE(create_time) order by name")
    List<ChartDataVO> getUserGrowthTrend();

    /**
     * 图表：用户状态分布 (饼图)
     * @return
     */
    @Select("select case when status = 1 then '正常用户' else '被拉黑/禁用' end as name, count(id) as value from user group by status")
    List<ChartDataVO> getUserStatusDistribution();
}
