package com.mjc.controller;


import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import com.mjc.bean.PageResult;
import com.mjc.bean.Result;
import com.mjc.service.CultureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cultures")
public class CultureController {

    @Autowired
    private CultureService cultureService;

    /*
     * 对文化进行分页查询
     * @return
     */
    @GetMapping
    public Result getCultureList(CultureQueryParam cultureQueryParam) {
        log.info("分页查询文化");
        PageResult page = cultureService.getCultureLists(cultureQueryParam);
        return Result.success(page);
    }

    /**
     * 添加文化
     * @return
     */
    @PostMapping
    public Result addCulture(@RequestBody Culture culture) {
        log.info("添加文化: {}", culture);
        cultureService.insertCulture(culture);
        return Result.success(culture);
    }

    /**
     * 根据id查询文化
     * @return
     */
    @GetMapping("/{id}")
    public Result getCultureById(@PathVariable Integer id){
        log.info("查询文件的id是: {}", id);
        Culture culture = cultureService.getCulture(id);
        return Result.success(culture);
    }

    /**
     * 修改文化
     * @param culture
     * @return
     */
    @PutMapping
    public Result updateCulture(@RequestBody Culture culture){
        log.info("更新文化: {}", culture);
        cultureService.updateCulture(culture);
        return Result.success();
    }

    /**
     * 根据id批量删除文化
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public Result deleteCulture(@PathVariable List<Integer> ids){
        log.info("批量删除文化: {}", ids);
        cultureService.deleteCulture(ids);
        return Result.success();
    }

    /**
     * 查询所有文化
     * @return
     */
    @GetMapping("/all")
    public Result findCulture() {
        log.info("查询所有文化");
        List<Culture> cultures = cultureService.findCulture();
        return Result.success(cultures);
    }
}
