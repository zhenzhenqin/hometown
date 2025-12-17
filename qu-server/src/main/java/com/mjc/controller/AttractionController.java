package com.mjc.controller;

import cn.hutool.core.util.BooleanUtil;
import com.mjc.Result.Result;
import com.mjc.Result.PageResult;
import com.mjc.queryParam.AttractionQueryParam;
import com.mjc.entity.Attraction;
import com.mjc.service.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mjc.contant.RedisConstants.ATTRACTION_LIKED_KEY;

@Slf4j
@Tag(name = "景区相关接口")
@RestController
@RequestMapping("/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    /**
     * 点赞功能
     * @param id
     * @return
     */
    @Operation(summary = "点赞功能")
    @GetMapping("/likes/{id}")
    public Result queryBlogLikes(@PathVariable Integer id, HttpServletRequest request){
        log.info("点赞的景点id为: {}", id);
        //获取登录用户id
        //存在localstorage中 前端通过请求头将用户id发送到后端
        String userIdStr = request.getHeader("User-ID");

        if (userIdStr == null || userIdStr.isEmpty()) {
            return Result.error("未获取到用户信息");
        }

        Long userId = Long.valueOf(userIdStr);

        //判断当前景区是否被当前用户点过赞
        String key = ATTRACTION_LIKED_KEY + id;

        //通过set判断里面是否存在该用户 来判断
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());

        if (BooleanUtil.isFalse(isMember)) {
            //如果不存在 则说明未点赞 那么可以进行点赞
            boolean isSuccess = attractionService.liked(id);

            //将用户保存到redis中
            if (isSuccess){
                stringRedisTemplate.opsForSet().add(key, userId.toString());
            }
        } else {
            //走到此处说明已经点赞了，则取消点赞

            //首先要将数据库减少1
            boolean isSuccess = attractionService.disLiked(id);

            //4.2 把用户从Redis的set集合移除
            if(isSuccess){
                stringRedisTemplate.opsForSet().remove(key,userId.toString());
            }
        }

        return Result.success();
    }
}
