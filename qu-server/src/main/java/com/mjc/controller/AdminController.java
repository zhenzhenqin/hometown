package com.mjc.controller;

import com.mjc.Result.PageResult;
import com.mjc.Result.Result;
import com.mjc.annotation.AutoLog;
import com.mjc.contant.JwtClaimsConstant;
import com.mjc.dto.AdminDTO;
import com.mjc.utils.IpUtil;
import com.mjc.utils.LocationUtil;
import com.mjc.vo.AdminListVO;
import com.mjc.dto.AdminLoginDTO;
import com.mjc.dto.AdminPasswordEditDTO;
import com.mjc.entity.Admin;
import com.mjc.properties.JwtProperties;
import com.mjc.queryParam.AdminQueryParam;
import com.mjc.service.AdminService;
import com.mjc.vo.AdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    @AutoLog("管理员登录")
    @PostMapping("/login")
    @Operation(summary = "管理员登陆")
    public Result<AdminVO> login(@RequestBody AdminLoginDTO adminLoginDTO, HttpServletRequest request){
        log.info("登录管理员信息为:{}", adminLoginDTO);

        Admin admin = adminService.login(adminLoginDTO);

        //获取客户端的ip地址
        String clientIp = IpUtil.getIpAddr(request);

        //获取当前ip的城市信息
        String cityInfo = LocationUtil.getCityInfo(clientIp);

        //将ip地址以及所在城市存入数据库
        Admin admin1 = Admin.builder()
                .id(admin.getId())
                .ip(clientIp)
                .city(cityInfo)
                .build();
        adminService.updateIpAndCity(admin1);

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
    @GetMapping("/query")
    @Operation(summary = "条件分页查询管理员")
    public Result<PageResult> query(AdminQueryParam adminQueryParam){
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
    public Result<AdminListVO> getUserById(@PathVariable Integer id){
        log.info("根据id回显管理员户信息: {}", id);
        AdminListVO adminListVO = adminService.getAdminById(id);
        return Result.success(adminListVO);
    }

    /**
     * 新增管理员
     * @param adminDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增管理员")
    public Result save(@RequestBody AdminDTO adminDTO){
        log.info("新增管理员:{}", adminDTO);
        adminService.save(adminDTO);
        return Result.success();
    }

    /**
     * 启用禁用管理员账号
     * @param status 管理员账号状态
     * @param id 管理员id
     * @return
     */
    @AutoLog("启用禁用管理员账号")
    @Operation(summary = "启用禁用管理员账号")
    @PostMapping("/status/{status}")
    public Result startOrStopAdmin(@PathVariable Integer status, Long id){
        log.info("启用管理员账号的参数: {}, {}", status, id);
        adminService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 更新管理员
     * @param admin
     * @return
     */
    @AutoLog("更新管理员")
    @PutMapping()
    @Operation(summary = "更新管理员")
    public Result updateUser(@RequestBody Admin admin){
        log.info("更新管理员: {}", admin);
        adminService.updateAdmin(admin);
        return Result.success();
    }

    /**
     * 编辑管理员密码
     * @param adminPasswordEditDTO 管理员id 旧密码 新密码
     * @return
     */
    @PutMapping("/editPassword")
    @Operation(summary = "编辑管理员密码")
    public Result update(@RequestBody AdminPasswordEditDTO adminPasswordEditDTO){
        log.info("更新管理员密码:{}", adminPasswordEditDTO);
        adminService.updatePassword(adminPasswordEditDTO);
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
