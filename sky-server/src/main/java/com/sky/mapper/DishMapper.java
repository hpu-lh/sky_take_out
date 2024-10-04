package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    Page<DishVO> selectAllDishForPage(DishPageQueryDTO dishPageQueryDTO);

    Integer deleteDishByIds(List<Integer> ids);

    Dish selectDishById(Integer id);

    DishVO selectDishVoById(Integer id);

    void updateDish(Dish dish);

    Integer updateDishStatus(@Param("dishId") Integer dishId, @Param("status") Integer status);

    List<Dish> selectDishListByCategoryId(Integer categoryId);

    List<Dish> selectEnableDishListByCategoryId(Dish dish);

    Integer countByMap(Map map);
}
