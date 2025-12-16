package com.mjc.controller;

import com.mjc.Result.PageResult;
import com.mjc.Result.Result;
import com.mjc.contant.JwtClaimsConstant;
import com.mjc.dto.AdminDTO;
import com.mjc.dto.AdminLoginDTO;
import com.mjc.entity.Admin;
import com.mjc.properties.JwtProperties;
import com.mjc.queryParam.AdminQueryParam;
import com.mjc.service.AdminService;
import com.mjc.vo.AdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mjc.utils.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员相关接口
 */
@Slf4j
@RestController
@Tag(name = "管理员相关接口")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录相关接口
     * @param adminLoginDTO 员工dto
     * @return 管理员vo
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登陆")
    public Result<AdminVO> login(@RequestBody AdminLoginDTO adminLoginDTO){
        log.info("登录管理员信息为:{}", adminLoginDTO);

        Admin admin = adminService.login(adminLoginDTO);

        //登录成功后 生成jwt令牌 存入管理员的id
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ADMIN_ID, admin.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);

        AdminVO adminVo = AdminVO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .token(token)
                .build();

        return Result.success(adminVo);
    }

    /**
     * 查询管理员
     * @return
     */
    @GetMapping()
    @Operation(summary = "查询管理员")
    public Result<Admin> getAdmin(){
        log.info("查询管理员");
        Admin admin = adminService.getAdmin();
        return Result.success(admin);
    }

    /**
     * 分页查询用户
     * @param adminQueryParam
     * @return
     */
    @GetMapping
    public Result query(AdminQueryParam adminQueryParam){
        log.info("分页查询参数:{}",adminQueryParam);
        PageResult result = adminService.queryAllAdmin(adminQueryParam);
        return Result.success(result);
    }

    /**
     * 根据id回显管理员信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id回显管理员信息")
    public Result<Admin> getUserById(@PathVariable Integer id){
        log.info("根据id回显管理员户信息: {}", id);
        Admin admin = adminService.getAdminById(id);
        return Result.success(admin);
    }

    /**
     * 更新管理员
     * @param admin
     * @return
     */
    @PutMapping()
    @Operation(summary = "更新管理员")
    public Result updateUser(@RequestBody Admin admin){
        log.info("更新管理员: {}", admin);
        adminService.updateAdmin(admin);
        return Result.success();
    }

    /**
     * 查询管理员
     * @return
     */
    @GetMapping("/all")
    @Operation(summary = "查询管理员")
    public Result<List<Admin>> findUser(){
        log.info("查询用户");
        List<Admin> adminList = adminService.findAdminList();
        return Result.success(adminList);
    }
}
