package com.mjc.mapper;

import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CultureMapper {

    /**
     * 条件查询文化列表
     * @param cultureQueryParam
     * @return
     */
    List<Culture> getCultureLists(CultureQueryParam cultureQueryParam);

    /**
     * 新增文化
     * @param culture
     */
    @Insert("insert into culture(title,content,image,create_time,update_time) values(#{title},#{content},#{image},#{createTime},#{updateTime})")
    void insertCulture(Culture culture);

    /**
     * 根据id查询文化
     * @param id
     * @return
     */
    @Select("select * from culture where id = #{id}")
    Culture getById(Integer id);

    /**
     * 修改文化
     * @param culture
     */
    @Update("update culture set title=#{title},content=#{content},image=#{image},update_time=#{updateTime} where id=#{id}")
    void updateCulture(Culture culture);

    /**
     * 根据id批量删除文化
     * @param ids
     */
    void deleteCulture(List<Integer> ids);
}
