package com.mjc.service;

import com.mjc.bean.User;

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
    User findUser();
}
