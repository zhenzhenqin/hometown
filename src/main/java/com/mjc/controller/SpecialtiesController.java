package com.mjc.controller;

import com.mjc.bean.PageResult;
import com.mjc.bean.Result;
import com.mjc.bean.Specialties;
import com.mjc.bean.SpecialtiesQueryParam;
import com.mjc.service.SpecialtiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    @PostMapping
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
    @PutMapping
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
    @DeleteMapping("/{ids}")
    public Result deleteByIds(@PathVariable List<Integer> ids){
        log.info("删除特产的id列表: {}",ids);
        specialtiesService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/all")
    public Result findSpecialties(){
        List<Specialties> specialties = specialtiesService.findSpecialties();
        return Result.success(specialties);
    }
}
