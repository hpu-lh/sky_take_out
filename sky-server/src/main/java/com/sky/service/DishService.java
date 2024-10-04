package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    public void addDishAndFlavor(DishDTO dishDTO);

    public PageResult queryAllDishForPage(DishPageQueryDTO dishPageQueryDTO);
    public Result deleteDishByIds(List<Integer> ids);
    public Result<DishVO> selectDishById(Integer id);
    public Result updateDish(DishDTO dishDTO);
    public Result updateDishStatus(Integer dishId,Integer status);
    public Result queryDishListByCategoryId(Integer categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
