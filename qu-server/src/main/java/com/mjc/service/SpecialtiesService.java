package com.mjc.service;

import com.mjc.Result.PageResult;
import com.mjc.queryParam.SpecialtiesQueryParam;
import com.mjc.entity.Specialties;

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

    /**
     * 查询所有特产
     * @return
     */
    List<Specialties> findSpecialties();
}
