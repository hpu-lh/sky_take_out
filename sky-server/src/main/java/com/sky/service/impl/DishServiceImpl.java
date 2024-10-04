package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("dishService")
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void addDishAndFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //向菜品表插入一条数据
        dishMapper.insertDish(dish);
        Long id = dish.getId();

        //向口味表插入多条数据
        List<DishFlavor> flavorList = dishDTO.getFlavors();
        if(flavorList!=null && flavorList.size()>0){
            for (DishFlavor dishFlavor:flavorList) {
                dishFlavor.setDishId(id);
            }
            dishFlavorMapper.insertDishFlavorByList(flavorList);
        }
    }

    @Override
    public PageResult queryAllDishForPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.selectAllDishForPage(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Transactional
    @Override
    public Result deleteDishByIds(List<Integer> ids) {
        //判断是否有起售中的菜品
        for(Integer id:ids){
            Dish dish = dishMapper.selectDishById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断是否有菜品被关联
        List<Integer> setmealDishIdList = setmealDishMapper.selectSetmealIdsByDishIds(ids);
        if(setmealDishIdList!=null && setmealDishIdList.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //删除菜品
        dishMapper.deleteDishByIds(ids);
        //删除口味
        dishFlavorMapper.deleteDishFlavorByDishId(ids);
        return Result.success();
    }

    @Override
    public Result<DishVO> selectDishById(Integer id) {
        //查询dish,分装成dishvo对象
        DishVO dishVO = dishMapper.selectDishVoById(id);
        //查询dishfalvor
        List<DishFlavor> dishFlavorList = dishFlavorMapper.selectDishFlavorListByDishId(id);
        dishVO.setFlavors(dishFlavorList);
        return Result.success(dishVO);
    }

    @Transactional
    @Override
    public Result updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(BaseContext.getCurrentId());
        dishMapper.updateDish(dish);

        List<Integer> ids=new ArrayList<>();
        ids.add(dishDTO.getId().intValue());
        dishFlavorMapper.deleteDishFlavorByDishId(ids);

        Long id=dishDTO.getId();
        //向口味表插入多条数据
        List<DishFlavor> flavorList = dishDTO.getFlavors();
        if(flavorList!=null && flavorList.size()>0){
            for (DishFlavor dishFlavor:flavorList) {
                dishFlavor.setDishId(id);
            }
            dishFlavorMapper.insertDishFlavorByList(flavorList);
        }
        return Result.success();
    }

    @Override
    public Result updateDishStatus(Integer dishId, Integer status) {
        Integer i = dishMapper.updateDishStatus(dishId, status);
        if(i>0){
            if(i==1){
                return Result.success();
            }else {
                return Result.error(MessageConstant.UPDATE_DISH_STATUS_FILED);
            }

        }else{
            return Result.error(MessageConstant.UPDATE_DISH_STATUS_FILED);
        }
    }

    @Override
    public Result queryDishListByCategoryId(Integer categoryId) {
        List<Dish> dishes = dishMapper.selectDishListByCategoryId(categoryId);
        return Result.success(dishes);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.selectEnableDishListByCategoryId(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectDishFlavorListByDishId(d.getId().intValue());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
