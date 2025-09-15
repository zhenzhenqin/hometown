package com.mjc.mapper;

import com.mjc.bean.Specialties;
import com.mjc.bean.SpecialtiesQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SpecialtiesMapper {

    /**
     * 对特产进行条件分页查询
     * @param specialtiesQueryParam
     * @return
     */
    List<Specialties> list(SpecialtiesQueryParam specialtiesQueryParam);

    /**
     * 添加特产
     * @param specialties
     */
    @Insert("insert into specialty(name, description, price, image, create_time, update_time) " +
            "values(#{name}, #{description}, #{price}, #{image}, #{createTime}, #{updateTime})")
    void add(Specialties specialties);

    /**
     * 根据id查询特产
     * @param id
     * @return
     */
    @Select("select * from specialty where id = #{id}")
    Specialties getById(Integer id);

    /*
     * 修改特产
     * @param specialties
     */
    @Update("update specialty set name = #{name}, description = #{description}, price = #{price}, image = #{image}, update_time = #{updateTime} where id = #{id}")
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
    @Select("select * from specialty")
    List<Specialties> findSpecialties();
}
