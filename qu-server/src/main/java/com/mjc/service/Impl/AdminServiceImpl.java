package com.mjc.service.Impl;

import com.mjc.contant.MessageConstant;
import com.mjc.contant.StatusConstant;
import com.mjc.dto.AdminDTO;
import com.mjc.entity.Admin;
import com.mjc.mapper.AdminMapper;
import com.mjc.service.AdminService;
import com.mjc.context.BaseContext;
import com.mjc.exception.AccountLockedException;
import com.mjc.exception.AccountNotFoundException;
import com.mjc.exception.PasswordErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 管理员登录
     * @param adminDTO
     * @return
     */
    @Override
    public Admin login(AdminDTO adminDTO) {
        String username = adminDTO.getUsername();
        String oldPassword = adminDTO.getPassword();


        //根据账号密码去数据库查询
        Admin admin = adminMapper.getByUserName(username);

        //如果管理员不存在
        if (admin == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        String password = admin.getPassword();

        //将输入的密码进行MD5加密后然后比对
        String newPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        //不一定则密码错误
        if (!newPassword.equals(password)) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //如果管理员账号被锁定
        if (admin.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //BaseContext.setCurrentId(admin.getId().longValue());

        //fanhuui
        return admin;
    }

    /**
     * 查询管理员
     * @return
     */
    @Override
    public Admin getAdmin() {
        Long currentId = BaseContext.getCurrentId();
        Admin admin = adminMapper.getAdmin(currentId);
        return admin;
    }

    /**
     * 根据id查询管理员
     * @param id
     * @return
     */
    @Override
    public Admin getAdminById(Integer id) {
        Admin admin = adminMapper.getById(id);
        return admin;
    }

    /**
     * 更新管理员信息
     * @param admin
     */
    @Override
    public void updateAdmin(Admin admin) {
        admin.setUpdateTime(LocalDateTime.now());
        adminMapper.updateAdmin(admin);
    }

    /**
     * 查询所有管理员
     * @return
     */
    @Override
    public List<Admin> findAdminList() {
        return adminMapper.findAllAdmin();
    }
}
