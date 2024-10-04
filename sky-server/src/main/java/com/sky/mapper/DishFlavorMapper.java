package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void insertDishFlavorByList(List<DishFlavor> dishFlavorList);

    Integer deleteDishFlavorByDishId(List<Integer> ids);

    List<DishFlavor> selectDishFlavorListByDishId(Integer id);

    void updateDishFlavor();
}
