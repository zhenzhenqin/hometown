package com.mjc.service.Impl;

import com.mjc.entity.User;
import com.mjc.mapper.UserMapper;
import com.mjc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户
     *
     * @return
     */
    @Override
    public User getUser() {
        User user = userMapper.getUser();
        return user;
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        User user = userMapper.getUserById(id);
        return user;
    }

    /**
     * 更新用户
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    /**
     *  查询用户
     * @return
     */
    @Override
    public List<User> findUser() {
        List<User> userList = userMapper.findUser();
        return userList;
    }
}