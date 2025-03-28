package com.lim.service.impl;

import com.lim.context.BaseContext;
import com.lim.dto.ShoppingCartDTO;
import com.lim.entity.Dish;
import com.lim.entity.Setmeal;
import com.lim.entity.ShoppingCart;
import com.lim.mapper.DishMapper;
import com.lim.mapper.SetMealMapper;
import com.lim.mapper.ShopCartMapper;
import com.lim.service.ShopCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ShopCartServiceImpl implements ShopCartService {
    private final ShopCartMapper shopCartMapper;
    private final DishMapper dishMapper;
    private final SetMealMapper setMealMapper;

    public ShopCartServiceImpl(ShopCartMapper shopCartMapper, DishMapper dishMapper, SetMealMapper setMealMapper) {
        this.shopCartMapper = shopCartMapper;
        this.dishMapper = dishMapper;
        this.setMealMapper = setMealMapper;
    }

    @Override
    public void save(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //查询购物车表所添加的东西
        List<ShoppingCart> shoppingCarts = shopCartMapper.selectShoppingCart(shoppingCart);
        //购物车中已经存在
        if (!shoppingCarts.isEmpty()) {
            //则该菜品/套餐数量加1
            ShoppingCart cart = shoppingCarts.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shopCartMapper.updateNumber(cart);
        } else {
            //购物车中不存在
            Long dishId = shoppingCart.getDishId();
            if (dishId != null) {
                //准备向购物车中增加一个菜品
                Dish dish = dishMapper.selectDishById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                //准备向购物车中增加一个套餐
                Long setMealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setMealMapper.selectSetMealByIds(List.of(setMealId)).get(0);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shopCartMapper.save(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> selectAll() {
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        return shopCartMapper.selectAll(shoppingCart);
    }

    @Override
    public void deleteByUserId() {
        shopCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        //获得要要减少数量的物品现有数量
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shopCartMapper.selectShoppingCart(shoppingCart);
        ShoppingCart item = shoppingCarts.get(0);
        //这里number >= 1,shopCartId必存在
        int number = item.getNumber();
        Long shopCartId = item.getId();

        //number == 1,直接删除
        if (number == 1) {
            shopCartMapper.deleteByShopCartId(shopCartId);
            return;
        }
        //number > 1,对应物品数量减1
        item.setNumber(item.getNumber() - 1);
        shopCartMapper.updateNumber(item);

    }
}
