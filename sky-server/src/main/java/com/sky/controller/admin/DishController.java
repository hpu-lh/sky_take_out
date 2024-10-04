package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.addDishAndFlavor(dishDTO);
        //清理缓存
        String key="dish_"+dishDTO.getCategoryId();
        //redisTemplate.delete(key);
        cleanCache(key);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("查询菜品")
    public Result<PageResult> queryAllDishForPage(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult = dishService.queryAllDishForPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteDishs(@RequestParam List<Integer> ids){
        log.info("菜品删除：{}",ids);
        try{
            dishService.deleteDishByIds(ids);
            //将所有菜品缓存数据清除
            //Set keys = redisTemplate.keys("dish_*");
            //redisTemplate.delete(keys);
            cleanCache("dish_*");
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据dishid查询dish信息")
    public Result<DishVO> selectDishInfo(@PathVariable Integer id){
        Result result=dishService.selectDishById(id);
        return result;
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        //将所有菜品缓存数据清除
        //Set keys = redisTemplate.keys("dish_*");
        cleanCache("dish_*");
        return dishService.updateDish(dishDTO);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品状态")
    public Result updateDishStatus(@RequestParam(value = "id") Integer dishId,@PathVariable Integer status){
        log.info("修改菜品参数：{}",dishId,status);
        //将所有菜品缓存数据清除
        //Set keys = redisTemplate.keys("dish_*");
        cleanCache("dish_*");
       return dishService.updateDishStatus(dishId,status);
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询dish得到列表")
    public Result queryDishListByCategoryId(@RequestParam Integer categoryId){
        log.info("菜品分类id：{}",categoryId);
        Result result = dishService.queryDishListByCategoryId(categoryId);
        return result;
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
