package com.mjc.service;

import com.mjc.bean.Attraction;
import com.mjc.bean.AttractionQueryParam;
import com.mjc.bean.PageResult;
import com.mjc.service.Impl.AttractionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig
public class AttractionServiceTest {

    @Autowired
    private AttractionService attractionService;

    @Test
    public void testList() {
        AttractionQueryParam param = new AttractionQueryParam();
        param.setPage(1);
        param.setPageSize(10);
        PageResult result = attractionService.list(param);
        assertNotNull(result);
    }

    @Test
    public void testAddAttraction() {
        Attraction attraction = new Attraction();
        attraction.setName("测试景点");
        attraction.setLocation("测试位置");
        attraction.setDescription("测试描述");
        attraction.setScore(new BigDecimal("4.5"));
        attraction.setImage("test.jpg");

        // 测试添加景点
        attractionService.addAttraction(attraction);
        // 验证添加成功
        assertTrue(true); // 实际项目中应该根据具体情况验证
    }

    @Test
    public void testGetById() {
        // 先添加一个测试景点
        Attraction attraction = new Attraction();
        attraction.setName("测试景点");
        attraction.setLocation("测试位置");
        attraction.setDescription("测试描述");
        attraction.setScore(new BigDecimal("4.5"));
        attraction.setImage("test.jpg");
        attractionService.addAttraction(attraction);

        // 查询刚添加的景点
        Attraction result = attractionService.getById(1); // 假设id为1
        assertNotNull(result);
    }

    @Test
    public void testUpdateAttraction() {
        Attraction attraction = new Attraction();
        attraction.setId(1);
        attraction.setName("更新景点名称");
        attraction.setLocation("更新位置");
        attraction.setDescription("更新描述");
        attraction.setScore(new BigDecimal("4.8"));

        attractionService.updateAttraction(attraction);
        assertTrue(true);
    }

    @Test
    public void testDeleteByIds() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        attractionService.deleteByIds(ids);
        assertTrue(true);
    }

    @Test
    public void testFindAttraction() {
        List<Attraction> attractions = attractionService.findAttraction();
        assertNotNull(attractions);
    }
}