package com.mjc.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.Result.PageResult;
import com.mjc.contant.MessageConstant;
import com.mjc.contant.StatusConstant;
import com.mjc.dto.UserLoginDTO;
import com.mjc.dto.UserPasswordEditDTO;
import com.mjc.dto.UserRegisterDTO;
import com.mjc.entity.User;
import com.mjc.exception.*;
import com.mjc.mapper.UserMapper;
import com.mjc.queryParam.UserQueryParam;
import com.mjc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.mjc.contant.RedisConstants.CAPTCHA_QUERY_KEY;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
    public User userLogin(UserLoginDTO userLoginDTO, String ip) {
        String uuid = userLoginDTO.getUuid();
        String code = userLoginDTO.getCode();
        // 构造 Redis Key
        String verifyKey = CAPTCHA_QUERY_KEY + uuid;
        // 从 Redis 查询真实验证码
        String captcha = stringRedisTemplate.opsForValue().get(verifyKey);

        // 校验：Redis中是否存在（是否过期）以及是否匹配
        if (captcha == null || !captcha.equalsIgnoreCase(code)) {
            throw new CaptchaException(MessageConstant.CAPTCHA_ERROR_OR_EXPIRED);
        }
        // 校验通过后，删除 Redis 中的 Key，防止重复使用
        stringRedisTemplate.delete(verifyKey);

        String username = userLoginDTO.getUsername();

        //根据username去数据库中查询
        User user = userMapper.getUserByUsername(username);

        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //判断该ip是否被禁用
        //根据ip去数据库查询 将该ip所有用户查出来
        List<User> users = userMapper.getUserByIp(ip);

        //遍历用户 如果该ip下有人被禁用 则不允许登录
        for (User user1 : users){
            if (user1.getStatus() == StatusConstant.DISABLE){
                throw new AccountDisabledException(MessageConstant.ACCOUNT_DISABLED);
            }
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
    public User userRegister(UserRegisterDTO userRegisterDTO, String ipAddr) {
        String uuid = userRegisterDTO.getUuid();
        String code = userRegisterDTO.getCode();
        // 构造 Redis Key
        String verifyKey = CAPTCHA_QUERY_KEY + uuid;
        // 从 Redis 查询真实验证码
        String captcha = stringRedisTemplate.opsForValue().get(verifyKey);

        // 校验：Redis中是否存在（是否过期）以及是否匹配
        if (captcha == null || !captcha.equalsIgnoreCase(code)) {
            throw new CaptchaException(MessageConstant.CAPTCHA_ERROR_OR_EXPIRED);
        }
        // 校验通过后，删除 Redis 中的 Key，防止重复使用
        stringRedisTemplate.delete(verifyKey);

        //防小人开始
        //判断是否未可疑ip
        //根据ip去数据库查询 将该ip所有用户查出来
        List<User> userList = userMapper.getUserByIp(ipAddr);

        //遍历用户 如果该ip下有人被禁用 则不允许注册
        for (User user1 : userList){
            if (user1.getStatus() == StatusConstant.DISABLE){
                throw new AccountDisabledException(MessageConstant.ACCOUNT_DISABLED_REGISTER);
            }
        }

        //先判断数据库中有无该手机号， 有的化则抛出异常
        String phone = userRegisterDTO.getPhone();

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setPhone(phone);

        List<User> users = userMapper.queryAllUser(userQueryParam);

        /*for (User user : users){
            System.out.println("user的名字是" + user.getUsername());
        }*/

        if (!users.isEmpty()) {
            //能查询到用户
            throw new PhoneExistException(MessageConstant.PHONE_EXIST);
        }

        //手机号无法查询到用户 再去判断username有没有注册过
        UserQueryParam userQueryParam2 = new UserQueryParam();
        userQueryParam2.setUsername(userRegisterDTO.getUsername());

        List<User> users2 = userMapper.queryAllUser(userQueryParam2);

        if (!users2.isEmpty()) {
            throw new AccountExistException(MessageConstant.ACCOUNT_EXIST);
        }

        //返回前端密码
        String returnPass = userRegisterDTO.getPassword();

        //防小人结束 开始注册
        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .password(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()))
                .phone(userRegisterDTO.getPhone())
                .status(StatusConstant.ENABLE)
                .build();

        //持久化
        userMapper.registerUser(user);

        User registerUser = userMapper.getUserByUsername(userRegisterDTO.getUsername());
        registerUser.setPassword(returnPass); //将原密码返回用于登录

        return registerUser;
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

    /**
     * 启用禁用用户账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStopUser(Integer status, Long id) {
        User user = User.builder()
                .id(Math.toIntExact(id))
                .status(status)
                .updateTime(LocalDateTime.now())
                .build();

        userMapper.updateUser(user);
    }

    /**
     * 修改用户密码
     * @param userPasswordEditDTO
     */
    @Override
    public void userEditPassword(UserPasswordEditDTO userPasswordEditDTO) {
        //1. 根据id查询数据库是否有
        User user = userMapper.getUserById(Math.toIntExact(userPasswordEditDTO.getUserId()));

        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //2. 将输入的密码进行比对
        String oldPassword = userPasswordEditDTO.getOldPassword();
        //旧密码加密
        String md5OldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        //比对查询
        if (!md5OldPassword.equals(user.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        user.setPassword(DigestUtils.md5DigestAsHex(userPasswordEditDTO.getNewPassword().getBytes()));
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateUser(user);
    }

    /**
     * 更新用户登录时的ip地址和城市信息
     * @param user1
     */
    @Override
    public void updateIpAndCity(User user1) {
        userMapper.updateIpAndCity(user1);
    }
}