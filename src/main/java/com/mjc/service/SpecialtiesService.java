package com.mjc.service;

import com.mjc.bean.PageResult;
import com.mjc.bean.Specialties;
import com.mjc.bean.SpecialtiesQueryParam;

import java.util.List;

public interface SpecialtiesService {

    /**
     * 对特产进行条件分页查询
     * @param specialtiesQueryParam
     * @return
     */
    PageResult list(SpecialtiesQueryParam specialtiesQueryParam);

    /**
     * 新增特产
     * @param specialties
     */
    void add(Specialties specialties);

    /**
     *  根据
     * @param id
     * @return
     */
    Specialties getById(Integer id);

    /**
     * 修改特产
     * @param specialties
     */
    void update(Specialties specialties);

    /**
     * 根据id批量删除特产
     * @param ids
     */
    void deleteByIds(List<Integer> ids);
}
