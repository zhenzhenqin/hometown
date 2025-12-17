package com.mjc.service.Impl;

import com.mjc.mapper.AttractionMapper;
import com.mjc.mapper.CultureMapper;
import com.mjc.mapper.SpecialtiesMapper;
import com.mjc.mapper.UserMapper;
import com.mjc.service.ReportService;
import com.mjc.vo.DashboardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AttractionMapper attractionMapper;
    @Autowired
    private SpecialtiesMapper specialtiesMapper;
    @Autowired
    private CultureMapper cultureMapper;

    /**
     * 获取后台首页驾驶舱统计数据
     * @return
     */
    @Override
    public DashboardVO getIndexDashboard() {
        //用户总数 景点总数 特产总数 文化文章数
        Long userCount = userMapper.countAll();
        Long attractionCount = attractionMapper.countAll();
        Long specialtyCount = specialtiesMapper.countAll();
        Long cultureCount = cultureMapper.countAll();

        // 计算总点赞数
        Long totalLikes = attractionMapper.sumLikes();


        return DashboardVO.builder()
                .userCount(userCount)
                .attractionCount(attractionCount)
                .specialtyCount(specialtyCount)
                .cultureCount(cultureCount)
                .totalLikes(totalLikes)
                .build();
    }
}
