package com.mjc.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.Result.PageResult;
import com.mjc.contant.MessageConstant;
import com.mjc.contant.StatusConstant;
import com.mjc.dto.UserLoginDTO;
import com.mjc.dto.UserRegisterDTO;
import com.mjc.entity.User;
import com.mjc.exception.*;
import com.mjc.mapper.UserMapper;
import com.mjc.queryParam.UserQueryParam;
import com.mjc.service.UserService;
import org.springframework.beans.BeanUtils;
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

    /**
     * 用户注册接口
     * @param userRegisterDTO
     * @return
     */
    @Override
    public User userRegister(UserRegisterDTO userRegisterDTO) {
        //先判断数据库中有无该手机号， 有的化则抛出异常
        String phone = userRegisterDTO.getPhone();

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setPhone(phone);

        List<User> users = userMapper.queryAllUser(userQueryParam);

        if (users != null) {
            //能查询到用户
            throw new PhoneExistException(MessageConstant.PHONE_EXIST);
        }

        //手机号无法查询到用户 再去判断username有没有注册过
        UserQueryParam userQueryParam2 = new UserQueryParam();
        userQueryParam2.setUsername(userRegisterDTO.getUsername());

        List<User> users2 = userMapper.queryAllUser(userQueryParam2);

        if (users2 != null) {
            throw new AccountExistException(MessageConstant.ACCOUNT_EXIST);
        }

        //防小人结束 开始注册
        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .password(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()))
                .phone(userRegisterDTO.getPhone())
                .status(StatusConstant.ENABLE)
                .build();

        //持久化
        userMapper.registerUser(user);

        user.setPassword(userRegisterDTO.getPassword()); //将原密码返回用于登录

        return user;
    }

    /**
     * 条件查询所有用户
     * @param userQueryParam
     * @return
     */
    @Override
    public PageResult queryAllUser(UserQueryParam userQueryParam) {
        //防小人
        if(userQueryParam.getPage() == null){
            userQueryParam.setPage(1);
        }
        if(userQueryParam.getPageSize() == null){
            userQueryParam.setPageSize(10);
        }

        PageHelper.startPage(userQueryParam.getPage(), userQueryParam.getPageSize());

        List<User> userlist = userMapper.queryAllUser(userQueryParam);

        Page<User> p = (Page<User>) userlist;

        return new PageResult(p.getTotal(), p.getResult());
    }
}