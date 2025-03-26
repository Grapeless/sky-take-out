package com.lim.service;

import com.lim.dto.ShoppingCartDTO;
import com.lim.entity.ShoppingCart;

import java.util.List;

public interface ShopCartService {
    void save(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> selectAll();

    void deleteAll();
}
