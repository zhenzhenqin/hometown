package com.mjc.service;

import com.mjc.entity.Admin;
import com.mjc.entity.dto.AdminDTO;
import org.springframework.stereotype.Service;


public interface AdminService {
    /**
     * 管理员登录
     * @param adminDTO
     * @return
     */
    Admin login(AdminDTO adminDTO);

}
