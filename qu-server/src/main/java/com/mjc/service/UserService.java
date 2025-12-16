package com.mjc.service;

import com.mjc.dto.UserLoginDTO;
import com.mjc.entity.User;

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
}
