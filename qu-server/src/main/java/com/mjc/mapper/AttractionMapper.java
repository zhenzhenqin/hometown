package com.mjc.mapper;

import com.mjc.annotation.AutoFill;
import com.mjc.enumeration.OperationType;
import com.mjc.queryParam.AttractionQueryParam;
import com.mjc.entity.Attraction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AttractionMapper {

    /**
     * 分页查询景点
     * @param attractionQueryParam
     * @return
     */
    List<Attraction> list(AttractionQueryParam attractionQueryParam);

    /**
     * 添加景点
     * @param attraction
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into attraction(name, location, description,score, create_time, update_time) values(#{name}, #{location}, #{description}, #{score}, #{createTime}, #{updateTime})")
    void addAttraction(Attraction attraction);

    /**
     * 根据id
     * @param id 景点id
     * @return 景点
     */
    @Select("select * from attraction where id = #{id}")
    Attraction getById(Integer id);

    /**
     * 修改景点
     * @param attraction
     */
    @Update("update attraction set name = #{name}, location = #{location}, liked = #{liked}, disliked = #{disliked}, description = #{description}, score = #{score}, update_time = #{updateTime} where id = #{id}")
    void updateAttraction(Attraction attraction);

    /**
     * 批量删除景点
     * @param ids
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 查询所有景点
     * @return
     */
    @Select("select * from attraction")
    List<Attraction> findAttraction();

    /**
     * 统计数量
     * @return
     */
    @Select("select count(id) from attraction")
    Long countAll();

    /**
     * 统计全站点赞数量
     * @return
     */
    @Select("select sum(liked) from attraction")
    Long sumLikes();
}
