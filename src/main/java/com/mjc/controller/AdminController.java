package com.mjc.controller;

import com.mjc.constant.JwtClaimsConstant;
import com.mjc.entity.Admin;
import com.mjc.entity.Result;
import com.mjc.entity.dto.AdminDTO;
import com.mjc.entity.vo.AdminVO;
import com.mjc.properties.JwtProperties;
import com.mjc.service.AdminService;
import com.mjc.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
     * @param adminDTO 员工dto
     * @return 管理员vo
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登陆")
    public Result<AdminVO> login(@RequestBody AdminDTO adminDTO){
        log.info("登录管理员信息为:{}", adminDTO);

        Admin admin = adminService.login(adminDTO);

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

}
