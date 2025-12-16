package com.mjc.controller;

import com.mjc.entity.Admin;
import com.mjc.entity.Result;
import com.mjc.entity.dto.AdminDTO;
import com.mjc.entity.vo.AdminVO;
import com.mjc.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 登录相关接口
     * @param adminDTO 员工dto
     * @return 管理员vo
     */
    @PostMapping("/login")
    public Result<AdminVO> login(@RequestBody AdminDTO adminDTO){
        log.info("登录管理员信息为:{}", adminDTO);

        Admin admin = adminService.login(adminDTO);
    }

}
