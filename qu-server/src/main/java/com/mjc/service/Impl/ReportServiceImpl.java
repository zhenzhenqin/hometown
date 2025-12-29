package com.mjc.service.Impl;

import com.mjc.mapper.AttractionMapper;
import com.mjc.mapper.CultureMapper;
import com.mjc.mapper.SpecialtiesMapper;
import com.mjc.mapper.UserMapper;
import com.mjc.service.ReportService;
import com.mjc.vo.ChartDataVO;
import com.mjc.vo.DashboardVO;
import com.mjc.vo.UserRegionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 图表：景点人气 Top5 (点赞数)
     * @return
     */
    @Override
    public List<ChartDataVO> getTopPopular() {
        return attractionMapper.getTopPopular();
    }

    /**
     * 图表：用户注册趋势 (折线图)
     * @return
     */
    @Override
    public List<ChartDataVO> getUserGrowthTrend() {
        return userMapper.getUserGrowthTrend();
    }

    /**
     * 图表：用户状态分布 (饼图)
     * @return
     */
    @Override
    public List<ChartDataVO> getUserStatusDistribution() {
        return userMapper.getUserStatusDistribution();
    }

    /**
     * 图表：特产价格区间分布 (饼图/直方图)
     * @return
     */
    @Override
    public List<ChartDataVO> getSpecialtyPriceDistribution() {
        return specialtiesMapper.getPriceRangeDistribution();
    }

    /**
     * 查询地域用户
     * @return
     */
    public List<UserRegionVO> getUserRegionStats() {
        List<UserRegionVO> list = userMapper.countUserByRegion();

        // 过滤掉纯省份的数据（比如只有“浙江”，没有具体城市，很难在地图上标点）
        // 或者你可以把“浙江”映射到杭州的经纬度，看你业务需求。
        // 这里做简单的名称补全：
        for (UserRegionVO vo : list) {
            String name = vo.getName();
            if (!name.endsWith("市") && !name.endsWith("区") && !name.endsWith("州") && !name.endsWith("盟")) {
                // 简单粗暴加“市”
                vo.setName(name);
            }
        }
        return list;
    }
}
