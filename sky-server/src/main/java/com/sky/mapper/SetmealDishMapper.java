package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    List<Integer> selectSetmealIdsByDishIds(List<Integer> ids);

    Integer insertSetmealDishByList(List<SetmealDish> list);

    Integer deleteSetmealDishBySetmealIds(List<Integer> ids);

    List<SetmealDish> selectSetmealDishListBySetmealId(Integer id);

}
