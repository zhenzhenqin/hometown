package com.mjc.controller;

import com.mjc.Result.PageResult;
import com.mjc.Result.Result;
import com.mjc.annotation.AutoLog;
import com.mjc.dto.UserLoginDTO;
import com.mjc.dto.UserPasswordEditDTO;
import com.mjc.dto.UserRegisterDTO;
import com.mjc.entity.User;
import com.mjc.queryParam.UserQueryParam;
import com.mjc.service.UserService;
import com.mjc.utils.IpUtil;
import com.mjc.utils.LocationUtil;
import com.mjc.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * @param userLoginDTO
     * @return
     */
    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public Result<UserLoginVO> Login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){
        log.info("登录的用户信息:{}", userLoginDTO);

        //获取用户登录时的ip地址
        String ipAddr = IpUtil.getIpAddr(request);
        if (ipAddr == null || ipAddr.isEmpty()) {
            ipAddr = "未知";
        }

        User user = userService.userLogin(userLoginDTO, ipAddr);


        //获取登录时所在城市
        String cityInfo = LocationUtil.getCityInfo(ipAddr);
        if (cityInfo == null || cityInfo.isEmpty()) {
            cityInfo = "未知";
        }

        //封装
        User user1 = User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .ip(ipAddr)
                .city(cityInfo)
                .build();

        //更新城市信息
        userService.updateIpAndCity(user1);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * 用户注册接口
     * @param userRegisterDTO
     * @return
     */
    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public Result<UserLoginVO> register(@RequestBody UserRegisterDTO userRegisterDTO, HttpServletRequest request){
        log.info("用户注册信息为:{}", userRegisterDTO);

        //获取用户注册时的ip地址
        String ipAddr = IpUtil.getIpAddr(request);
        if (ipAddr == null || ipAddr.isEmpty()) {
            ipAddr = "未知";
        }

        User user = userService.userRegister(userRegisterDTO, ipAddr);


        //获取注册时所在城市
        String cityInfo = LocationUtil.getCityInfo(ipAddr);
        if (cityInfo == null || cityInfo.isEmpty()) {
            cityInfo = "未知";
        }

        //将城市信息保存到用户表中
        user.setIp(ipAddr);
        user.setRegisterCity(cityInfo);
        user.setCity(cityInfo);

        userService.updateIpAndCity(user);

        //回传注册信息用于登录
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * 用户修改密码功能
     * @param userPasswordEditDTO
     * @return
     */
    @PostMapping("/editPassword")
    @Operation(summary = "用户修改密码")
    public Result userEditPassword(@RequestBody UserPasswordEditDTO userPasswordEditDTO){
        log.info("用户修改密码: {}", userPasswordEditDTO);

        userService.userEditPassword(userPasswordEditDTO);

        return Result.success();
    }

    /**
     * 条件分页查询用户
     * @param userQueryParam
     * @return
     */
    @Operation(summary = "条件分页查询用户")
    @GetMapping("/query")
    public Result<PageResult> queryAllUser(UserQueryParam userQueryParam){
        log.info("分页查询用户参数:{}", userQueryParam);
        PageResult result = userService.queryAllUser(userQueryParam);
        return Result.success(result);
    }

    /**
     * 启用禁用用户
     * @param status
     * @param id
     * @return
     */
    @AutoLog("启用禁用用户接口")
    @Operation(summary = "启用禁用用户接口")
    @PostMapping("/status/{status}")
    public Result startOrStopUser(@PathVariable Integer status, Long id){
        log.info("启用禁用用户参数:{},{}", status, id);
        userService.startOrStopUser(status, id);
        return Result.success();
    }

    /**
     * 查询用户
     * @return
     */
    @GetMapping()
    @Operation(summary = "查询用户")
    public Result getUser(){
        log.info("查询用户");
        User user = userService.getUser();
        return Result.success(user);
    }

    /**
     * 根据id回显用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id回显用户信息")
    public Result getUserById(@PathVariable Integer id){
        log.info("根据id回显用户信息: {}", id);
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户")
    public Result updateUser(@RequestBody User user){
        log.info("更新用户: {}", user);
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 查询用户
     * @return
     */
    @GetMapping("/all")
    @Operation(summary = "查询用户")
    public Result<List<User>> findUser(){
        log.info("查询用户");
        List<User> userList = userService.findUser();
        return Result.success(userList);
    }
}
