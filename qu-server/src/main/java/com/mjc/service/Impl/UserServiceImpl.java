package com.mjc.service.Impl;

import com.mjc.contant.MessageConstant;
import com.mjc.contant.StatusConstant;
import com.mjc.dto.UserLoginDTO;
import com.mjc.entity.User;
import com.mjc.exception.AccountLockedException;
import com.mjc.exception.AccountNotFoundException;
import com.mjc.exception.PasswordErrorException;
import com.mjc.mapper.UserMapper;
import com.mjc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User userLogin(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();

        //根据username去数据库中查询
        User user = userMapper.getUserByUsername(username);

        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //拿到数据库中密码进行比对
        String oldPassword = userLoginDTO.getPassword();
        String md5OldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes()); //输入密码加密

        if (!md5OldPassword.equals(user.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 判断账号是否被拉黑
        if (user.getStatus() == StatusConstant.DISABLE){
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_BLACK);
        }

        return user;
    }
}