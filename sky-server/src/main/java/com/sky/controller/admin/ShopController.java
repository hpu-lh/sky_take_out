package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺接口")
public class ShopController {
    private static final String shopStatus="SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态为:{}",status);
        ValueOperations valueOperations= redisTemplate.opsForValue();
        valueOperations.set(shopStatus,status);
        return Result.success();
    }
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result getShopStatus(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer shop_status = (Integer) valueOperations.get(shopStatus);
        return Result.success(shop_status);
    }
}
