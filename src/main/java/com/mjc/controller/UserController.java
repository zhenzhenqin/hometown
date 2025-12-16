package com.mjc.controller;

import com.mjc.entity.Result;
import com.mjc.entity.User;
import com.mjc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
    @PutMapping()
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
    public Result findUser(){
        log.info("查询用户");
        User user = userService.findUser();
        return Result.success(user);
    }
}
