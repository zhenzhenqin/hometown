package com.mjc.controller;

import com.mjc.bean.Attraction;
import com.mjc.bean.AttractionQueryParam;
import com.mjc.bean.PageResult;
import com.mjc.bean.Result;
import com.mjc.service.AttractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    /**
     *  分页查询景点
     * @param attractionQueryParam
     * @return
     */
    @GetMapping
    public Result list(AttractionQueryParam attractionQueryParam) {
        log.info("条件分页查询: {}", attractionQueryParam);
        PageResult page = attractionService.list(attractionQueryParam);
        return Result.success(page);
    }

    /**
     * 添加景点
     * @param attraction
     * @return
     */
    @PostMapping
    public Result addAttraction(@RequestBody Attraction attraction) {
        log.info("添加景点: {}", attraction);
        attractionService.addAttraction(attraction);
        return Result.success();
    }

    /**
     * 根据id查询景点
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("查询id为{}的景点", id);
        Attraction attraction = attractionService.getById(id);
        return Result.success(attraction);
    }

    /**
     * 修改景点
     * @param attraction
     * @return
     */
    @PutMapping
    public Result updateAttraction(@RequestBody Attraction attraction) {
        log.info("修改景点: {}", attraction);
        attractionService.updateAttraction(attraction);
        return Result.success();
    }

    /**
     * 批量删除景点
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public Result deleteByIds(@PathVariable List<Integer> ids) {
        log.info("删除id为{}的景点", ids);
        attractionService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 查询所有景点
     * @return
     */
    @GetMapping("/all")
    public Result findAttraction(){
        List<Attraction> list = attractionService.findAttraction();
        return Result.success(list);
    }
}
