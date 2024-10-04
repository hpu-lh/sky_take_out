package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Results;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Transactional
    @Override
    public Result insertSetmealDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insertSetmeal(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> setmealDisheList = setmealDTO.getSetmealDishes();
        if(setmealDisheList!=null && setmealDisheList.size()>0){
            for(SetmealDish setmealDish:setmealDisheList){
                setmealDish.setSetmealId(id);
            }
            setmealDishMapper.insertSetmealDishByList(setmealDisheList);
        }
        return Result.success();
    }

    @Override
    public PageResult querySetmealListForPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOS = setmealMapper.selectSetmealListForPage(setmealPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(setmealVOS.getTotal());
        pageResult.setRecords(setmealVOS.getResult());
        return pageResult;
    }

    @Transactional
    @Override
    public Result deleteSetmeal(List<Integer> ids) {
        Setmeal setmeal = new Setmeal();
        for(Integer id:ids){
            setmeal=setmealMapper.selectSetmealById(id);
            if(setmeal.getStatus()==1){
                return Result.error(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealMapper.deleteSetmealByIds(ids);
        setmealDishMapper.deleteSetmealDishBySetmealIds(ids);
        return Result.success();
    }

    @Override
    public Result updateSetmealStatus(Integer id, Integer status) {
        Integer integer = setmealMapper.updateSetmealStatusById(id, status);
        if(integer>0){
            if(integer==1){
                return Result.success();
            }else {
                return Result.error(MessageConstant.UPDATE_DISH_STATUS_FILED);
            }
        }else{
            return Result.error(MessageConstant.UPDATE_DISH_STATUS_FILED);
        }
    }

    @Override
    public Result querySetmealById(Integer id) {
        SetmealVO setmealVO = setmealMapper.selectSetmealVOById(id);
        List<SetmealDish> setmealDisheList = setmealDishMapper.selectSetmealDishListBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDisheList);
        return Result.success(setmealVO);
    }

    @Transactional
    @Override
    public Result updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.updateSetmeal(setmeal);
        List<Integer> integers = new ArrayList<>();
        integers.add(setmealDTO.getId().intValue());
        setmealDishMapper.deleteSetmealDishBySetmealIds(integers);
        List<SetmealDish> setmealDisheList = setmealDTO.getSetmealDishes();
        if(setmealDisheList!=null && setmealDisheList.size()>0){
            for(SetmealDish setmealDish:setmealDisheList){
                setmealDish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insertSetmealDishByList(setmealDisheList);
        }
        return Result.success();
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }


}
