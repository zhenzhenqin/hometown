package com.mjc.service;

import com.mjc.Result.PageResult;
import com.mjc.annotation.AutoFill;
import com.mjc.dto.UserLoginDTO;
import com.mjc.dto.UserPasswordEditDTO;
import com.mjc.dto.UserRegisterDTO;
import com.mjc.entity.User;
import com.mjc.enumeration.OperationType;
import com.mjc.queryParam.UserQueryParam;

import java.util.List;

public interface UserService {

    /**
     * 查询用户
     * @return
     */
    User getUser();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    User getUserById(Integer id);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 查询所有用户
     * @return
     */
    List<User> findUser();

    /**
     * 用户登录接口实现
     * @param userLoginDTO
     * @return
     */
    User userLogin(UserLoginDTO userLoginDTO);

    /**
     * 用户注册接口
     * @param userRegisterDTO
     * @return
     */
    User userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 条件分页查询所有用户
     * @param userQueryParam
     * @return
     */
    PageResult queryAllUser(UserQueryParam userQueryParam);

    /**
     * 启用禁用用户账号
     * @param status
     * @param id
     */
    void startOrStopUser(Integer status, Long id);

    /**
     * 用户修改密码
     * @param userPasswordEditDTO
     */
    void userEditPassword(UserPasswordEditDTO userPasswordEditDTO);
}
