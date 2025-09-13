package com.mjc.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import com.mjc.bean.PageResult;
import com.mjc.mapper.CultureMapper;
import com.mjc.service.CultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CultureServiceImpl implements CultureService {

    @Autowired
    private CultureMapper cultureMapper;

    /**
     * 获取文化列表
     * @param cultureQueryParam
     * @return
     */
    @Override
    public PageResult getCultureLists(CultureQueryParam cultureQueryParam) {
        if(cultureQueryParam.getPage() == null){
            cultureQueryParam.setPage(1);
        }
        if(cultureQueryParam.getPageSize() == null){
            cultureQueryParam.setPageSize(10);
        }
        //配置分页参数，及第几页和每页展示条数
        PageHelper.startPage(cultureQueryParam.getPage(), cultureQueryParam.getPageSize());

        //查询
        List<Culture> cultureList = cultureMapper.getCultureLists(cultureQueryParam);
        Page<Culture> page = (Page<Culture>) cultureList; //将查询的文化列表强转为Page对象
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加文化
     * @param culture
     */
    @Override
    public void insertCulture(Culture culture) {
        culture.setCreateTime(LocalDateTime.now());
        culture.setUpdateTime(LocalDateTime.now());
        cultureMapper.insertCulture(culture);
    }
}
