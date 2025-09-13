package com.mjc.controller;


import com.mjc.bean.Culture;
import com.mjc.bean.CultureQueryParam;
import com.mjc.bean.PageResult;
import com.mjc.bean.Result;
import com.mjc.service.CultureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
