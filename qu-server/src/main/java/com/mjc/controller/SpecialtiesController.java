package com.mjc.controller;

import com.mjc.Result.Result;
import com.mjc.Result.PageResult;
import com.mjc.annotation.AutoLog;
import com.mjc.queryParam.SpecialtiesQueryParam;
import com.mjc.entity.Specialties;
import com.mjc.service.SpecialtiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "特产相关接口")
@RestController
@RequestMapping("/specialties")
public class SpecialtiesController {

    @Autowired
    private SpecialtiesService specialtiesService;

    /**
     * 对特产进行条件分页查询
     * @return
     */
    @GetMapping()
    @Operation(summary = "特产分页查询")
    public Result list(SpecialtiesQueryParam specialtiesQueryParam){
        log.info("specialtiesQueryParam:{}",specialtiesQueryParam);
        PageResult pageResult = specialtiesService.list(specialtiesQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增特产
     * @param specialties
     * @return
     */
    @AutoLog("新增特产")
    @PostMapping
    @Operation(summary = "新增特产")
    public Result addSpecialties(@RequestBody Specialties specialties){
        log.info("新增加的特产:{}",specialties);
        specialtiesService.add(specialties);
        return Result.success();
    }

    /**
     * 根据id查询特产
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询特产")
    public Result getById(@PathVariable Integer id){
        log.info("根据id查询特产: {}",id);
        Specialties specialties = specialtiesService.getById(id);
        return Result.success(specialties);
    }

    /**
     * 修改特产
     * @param specialties
     * @return
     */
    @AutoLog("修改特产")
    @PutMapping
    @Operation(summary = "修改特产")
    public Result updateSpecialties(@RequestBody Specialties specialties){
        log.info("修改特产: {}",specialties);
        specialtiesService.update(specialties);
        return Result.success();
    }

    /**
     *  根据id批量删除特产
     * @param ids
     * @return
     */
    @AutoLog("删除特产")
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除特产")
    public Result deleteByIds(@PathVariable List<Integer> ids){
        log.info("删除特产的id列表: {}",ids);
        specialtiesService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 查询所有特产
     * @return
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有特产")
    public Result findSpecialties(){
        List<Specialties> specialties = specialtiesService.findSpecialties();
        return Result.success(specialties);
    }
}
