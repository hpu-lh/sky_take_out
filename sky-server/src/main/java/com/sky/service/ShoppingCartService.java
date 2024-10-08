package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    public List<ShoppingCart> queryShoppingCart();

    public void deleteShoppingCart();
}
