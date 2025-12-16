package com.mjc.service.Impl;

import com.mjc.constant.MessageConstant;
import com.mjc.constant.StatusConstant;
import com.mjc.entity.Admin;
import com.mjc.entity.dto.AdminDTO;
import com.mjc.exception.AccountLockedException;
import com.mjc.exception.AccountNotFoundException;
import com.mjc.exception.PasswordErrorException;
import com.mjc.mapper.AdminMapper;
import com.mjc.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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

        //fanhuui
        return admin;
    }
}
