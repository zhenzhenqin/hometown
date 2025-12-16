package com.mjc.controller;

import com.mjc.entity.Attraction;
import com.mjc.entity.AttractionQueryParam;
import com.mjc.entity.PageResult;
import com.mjc.entity.Result;
import com.mjc.service.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "景区相关接口")
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
    @Operation(summary = "景区分页查询")
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
    @Operation(summary = "新增景区")
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
    @Operation(summary = "查询景区")
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
    @Operation(summary = "修改景区")
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
    @Operation(summary = "删除景区")
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
    @Operation(summary = "查询所有景点")
    public Result findAttraction(){
        List<Attraction> list = attractionService.findAttraction();
        return Result.success(list);
    }
}
