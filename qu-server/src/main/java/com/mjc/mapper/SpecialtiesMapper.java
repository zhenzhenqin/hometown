package com.mjc.mapper;

import com.mjc.annotation.AutoFill;
import com.mjc.enumeration.OperationType;
import com.mjc.queryParam.SpecialtiesQueryParam;
import com.mjc.entity.Specialties;
import com.mjc.vo.ChartDataVO;
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
    @AutoFill(value = OperationType.INSERT)
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
    @AutoFill(value = OperationType.UPDATE)
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

    /**
     * 统计数量
     * @return
     */
    @Select("select count(id) from specialty")
    Long countAll();

    /**
     * 获取特产价格区间分布
     * @return
     */
    @Select("SELECT " +
            "CASE " +
            "  WHEN price < 50 THEN '50元以下' " +
            "  WHEN price >= 50 AND price < 100 THEN '50-100元' " +
            "  WHEN price >= 100 AND price < 200 THEN '100-200元' " +
            "  WHEN price >= 200 AND price < 500 THEN '200-500元' " +
            "  ELSE '500元以上' " +
            "END AS name, " +
            "COUNT(*) AS value " +
            "FROM specialty " +
            "GROUP BY " +
            "CASE " +
            "  WHEN price < 50 THEN '50元以下' " +
            "  WHEN price >= 50 AND price < 100 THEN '50-100元' " +
            "  WHEN price >= 100 AND price < 200 THEN '100-200元' " +
            "  WHEN price >= 200 AND price < 500 THEN '200-500元' " +
            "  ELSE '500元以上' " +
            "END " +
            "ORDER BY MIN(price)")
    List<ChartDataVO> getPriceRangeDistribution();
}
