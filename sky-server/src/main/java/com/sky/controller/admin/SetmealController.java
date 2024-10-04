package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("添加套餐")
    @CacheEvict(cacheNames = "setmealCache",key="#setmealDTO.categoryId")
    public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("添加套餐：{}",setmealDTO);
        Result result = setmealService.insertSetmealDish(setmealDTO);
        return result;
    }

    @GetMapping("/page")
    @ApiOperation("查询套餐")
    public Result<PageResult> querySetmealForPage(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("查询套餐：{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.querySetmealListForPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result deleteSetmeal(@RequestParam List<Integer> ids){
        log.info("删除套餐:{}",ids);
        Result result = setmealService.deleteSetmeal(ids);
        return result;
    }

    @PostMapping("/status/{status}")
    @ApiOperation("更新商品售卖状态")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result updateSetmealStatus(@RequestParam Integer id,@PathVariable Integer status){
        log.info("更新商品:{}---{}",id,status);
        Result result = setmealService.updateSetmealStatus(id, status);
        return result;
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result querySetmealDish(@PathVariable Integer id){
        log.info("根据id查询套餐：{}",id);
        Result result = setmealService.querySetmealById(id);
        return result;
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}",setmealDTO);
        Result result = setmealService.updateSetmeal(setmealDTO);
        return result;
    }
}
