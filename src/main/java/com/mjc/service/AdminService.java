package com.mjc.service;

import com.mjc.entity.Admin;
import com.mjc.entity.dto.AdminDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminService {
    /**
     * 管理员登录
     * @param adminDTO
     * @return
     */
    Admin login(AdminDTO adminDTO);

    /**
     * 查询管理员
     * @return
     */
    Admin getAdmin();

    /**
     * 根据id查询管理员
     * @param id
     * @return
     */
    Admin getAdminById(Integer id);

    /**
     * 更新管理员
     * @param admin
     */
    void updateAdmin(Admin admin);

    /**
     * 查询所有管理员
     * @return
     */
    List<Admin> findAdminList();
}
