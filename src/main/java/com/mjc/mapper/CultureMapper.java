package com.mjc.mapper;

import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}
