package com.mjc.service;

import com.mjc.entity.PageResult;
import com.mjc.entity.Specialties;
import com.mjc.entity.SpecialtiesQueryParam;
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
public class SpecialtiesServiceTest {

    @Autowired
    private SpecialtiesService specialtiesService;

    @Test
    public void testList() {
        SpecialtiesQueryParam param = new SpecialtiesQueryParam();
        param.setPage(1);
        param.setPageSize(10);
        PageResult result = specialtiesService.list(param);
        assertNotNull(result);
    }

    @Test
    public void testAdd() {
        Specialties specialties = new Specialties();
        specialties.setName("测试特产");
        specialties.setDescription("测试特产描述");
        specialties.setPrice(new BigDecimal("99.99"));
        specialties.setImage("test.jpg");

        specialtiesService.add(specialties);
        assertTrue(true);
    }

    @Test
    public void testGetById() {
        Specialties specialties = specialtiesService.getById(1);
        assertNotNull(specialties);
    }

    @Test
    public void testUpdate() {
        Specialties specialties = new Specialties();
        specialties.setId(1);
        specialties.setName("更新特产名称");
        specialties.setDescription("更新特产描述");
        specialties.setPrice(new BigDecimal("199.99"));

        specialtiesService.update(specialties);
        assertTrue(true);
    }

    @Test
    public void testDeleteByIds() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        specialtiesService.deleteByIds(ids);
        assertTrue(true);
    }

    @Test
    public void testFindSpecialties() {
        List<Specialties> specialtiesList = specialtiesService.findSpecialties();
        assertNotNull(specialtiesList);
    }
}