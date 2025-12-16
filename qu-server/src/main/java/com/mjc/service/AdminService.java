package com.mjc.service;

import com.mjc.Result.PageResult;
import com.mjc.dto.AdminDTO;
import com.mjc.dto.AdminLoginDTO;
import com.mjc.dto.AdminPasswordEditDTO;
import com.mjc.entity.Admin;
import com.mjc.queryParam.AdminQueryParam;

import java.util.List;


public interface AdminService {
    /**
     * 管理员登录
     * @param adminLoginDTO
     * @return
     */
    Admin login(AdminLoginDTO adminLoginDTO);

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
    AdminDTO getAdminById(Integer id);

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

    /**
     * 分页查询所有管理员
     * @param adminQueryParam
     * @return
     */
    PageResult queryAllAdmin(AdminQueryParam adminQueryParam);

    /**
     * 启用禁用管理员账号
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     *  更新管理员密码
     * @param adminPasswordEditDTO
     */
    void updatePassword(AdminPasswordEditDTO adminPasswordEditDTO);
}
