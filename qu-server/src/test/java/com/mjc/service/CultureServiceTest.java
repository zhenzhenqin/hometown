package com.mjc.service;

import com.mjc.entity.Culture;
import com.mjc.entity.CultureQueryParam;
import com.mjc.entity.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig
public class CultureServiceTest {

    @Autowired
    private CultureService cultureService;

    @Test
    public void testGetCultureLists() {
        CultureQueryParam param = new CultureQueryParam();
        param.setPage(1);
        param.setPageSize(10);
        PageResult result = cultureService.getCultureLists(param);
        assertNotNull(result);
    }

    @Test
    public void testInsertCulture() {
        Culture culture = new Culture();
        culture.setTitle("测试文化");
        culture.setContent("测试文化内容");
        culture.setImage("test.jpg");

        cultureService.insertCulture(culture);
        assertTrue(true);
    }

    @Test
    public void testGetCulture() {
        Culture culture = cultureService.getCulture(1);
        assertNotNull(culture);
    }

    @Test
    public void testUpdateCulture() {
        Culture culture = new Culture();
        culture.setId(1);
        culture.setTitle("更新文化标题");
        culture.setContent("更新文化内容");

        cultureService.updateCulture(culture);
        assertTrue(true);
    }

    @Test
    public void testDeleteCulture() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        cultureService.deleteCulture(ids);
        assertTrue(true);
    }

    @Test
    public void testFindCulture() {
        List<Culture> cultures = cultureService.findCulture();
        assertNotNull(cultures);
    }
}