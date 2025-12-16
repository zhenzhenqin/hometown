package com.mjc.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.Result.PageResult;
import com.mjc.contant.MessageConstant;
import com.mjc.contant.PasswordConstant;
import com.mjc.contant.StatusConstant;
import com.mjc.dto.AdminDTO;
import com.mjc.vo.AdminListVO;
import com.mjc.dto.AdminLoginDTO;
import com.mjc.dto.AdminPasswordEditDTO;
import com.mjc.entity.Admin;
import com.mjc.mapper.AdminMapper;
import com.mjc.queryParam.AdminQueryParam;
import com.mjc.service.AdminService;
import com.mjc.context.BaseContext;
import com.mjc.exception.AccountLockedException;
import com.mjc.exception.AccountNotFoundException;
import com.mjc.exception.PasswordErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
     * @param adminLoginDTO
     * @return
     */
    @Override
    public Admin login(AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String oldPassword = adminLoginDTO.getPassword();


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
    public AdminListVO getAdminById(Integer id) {
        Admin admin = adminMapper.getById(id);

        //属性拷贝
        AdminListVO adminListVO = new AdminListVO();
        BeanUtils.copyProperties(admin, adminListVO);

        return adminListVO;
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

    /**
     * 分页查询所有管理员
     * @param adminQueryParam
     * @return
     */
    @Override
    public PageResult queryAllAdmin(AdminQueryParam adminQueryParam) {
        if(adminQueryParam.getPage() == null){
            adminQueryParam.setPage(1);
        }
        if(adminQueryParam.getPageSize() == null){
            adminQueryParam.setPageSize(10);
        }

        PageHelper.startPage(adminQueryParam.getPage(), adminQueryParam.getPageSize());

        List<Admin> list = adminMapper.queryAllAdmin(adminQueryParam);
        Page<Admin> p = (Page<Admin>) list;

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 启用禁用管理员账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {

        Admin admin = Admin.builder()
                .id(id.intValue())
                .status(status)
                .updateTime(LocalDateTime.now())
                .build();

        adminMapper.updateAdmin(admin);
    }

    /**
     * 更新管理员密码
     * @param adminPasswordEditDTO
     */
    @Override
    public void updatePassword(AdminPasswordEditDTO adminPasswordEditDTO) {
        //1. 根据id查询数据库是否有
        Admin admin = adminMapper.getById(Math.toIntExact(adminPasswordEditDTO.getAdminId()));

        if (admin == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //2. 将输入的密码进行比对
        String oldPassword = adminPasswordEditDTO.getOldPassword();
        //旧密码加密
        String md5OldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        //比对查询
        if (!md5OldPassword.equals(admin.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        admin.setPassword(DigestUtils.md5DigestAsHex(adminPasswordEditDTO.getNewPassword().getBytes()));
        admin.setUpdateTime(LocalDateTime.now());

        adminMapper.updateAdmin(admin);
    }

    /**
     * 新增管理员
     * @param adminDTO
     */
    @Override
    public void save(AdminDTO adminDTO) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminDTO, admin);

        admin.setStatus(StatusConstant.ENABLE);

        admin.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());

        adminMapper.save(admin);
    }
}
