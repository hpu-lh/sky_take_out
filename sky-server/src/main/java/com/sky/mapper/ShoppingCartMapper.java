package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> selectShoppingCart(ShoppingCart shoppingCart);

    void updateShoppingCartNumberById(ShoppingCart shoppingCart);

    void insertShoppingCart(ShoppingCart shoppingCart);

    void deleteShoppingCartByUserId(Long userId);

    /**
     * 批量插入购物车数据
     *
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
