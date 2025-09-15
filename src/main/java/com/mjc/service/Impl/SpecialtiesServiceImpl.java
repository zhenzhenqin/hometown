package com.mjc.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.bean.PageResult;
import com.mjc.bean.Specialties;
import com.mjc.bean.SpecialtiesQueryParam;
import com.mjc.mapper.SpecialtiesMapper;
import com.mjc.service.SpecialtiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpecialtiesServiceImpl implements SpecialtiesService {

    @Autowired
    private SpecialtiesMapper specialtiesMapper;

    /**
     * 对特产进行条件分页查询
     * @param specialtiesQueryParam
     * @return
     */
    @Override
    public PageResult list(SpecialtiesQueryParam specialtiesQueryParam) {

        //如果参数为空 则赋默认值 防止抛出空指针异常
        if(specialtiesQueryParam.getPage() == null){
            specialtiesQueryParam.setPage(1);
        }
        if(specialtiesQueryParam.getPageSize() == null){
            specialtiesQueryParam.setPageSize(10);
        }
        //配置分页参数，及第几页和每页展示条数
        PageHelper.startPage(specialtiesQueryParam.getPage(), specialtiesQueryParam.getPageSize());
        //开始分页
        List<Specialties> specialtiesList = specialtiesMapper.list(specialtiesQueryParam);
        Page<Specialties> page = (Page<Specialties>) specialtiesList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增特产
     * @param specialties
     */
    @Override
    public void add(Specialties specialties) {
        specialties.setCreateTime(LocalDateTime.now());
        specialties.setUpdateTime(LocalDateTime.now());
        specialtiesMapper.add(specialties);
    }

    /**
     * 根据id查询特产
     * @param id
     * @return
     */
    @Override
    public Specialties getById(Integer id) {
        Specialties specialties = specialtiesMapper.getById(id);
        return specialties;
    }

    /**
     * 修改特产
     * @param specialties
     */
    @Override
    public void update(Specialties specialties) {
        specialties.setUpdateTime(LocalDateTime.now());
        specialtiesMapper.update(specialties);
    }

    /**
     * 根据id批量删除特产
     * @param ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        specialtiesMapper.deleteByIds(ids);
    }

    /**
     * 查询所有特产
     * @return
     */
    @Override
    public List<Specialties> findSpecialties() {
        List<Specialties> specialtiesList = specialtiesMapper.findSpecialties();
        return specialtiesList;
    }
}
