package com.lim.mapper;

import com.lim.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShopCartMapper {

    /**
     * 根据setMealId,dishId,dishFlavor查询ShoppingCart对象
     */
    List<ShoppingCart> selectShoppingCart(ShoppingCart shoppingCart);

    /**
     * 给指定的ShoppingCart的number加一
     * @param cart
     */
    void updateNumber(ShoppingCart cart);

    /**
     * 插入一个shoppingCart
     * @param shoppingCart 要插入的对象
     */
    void save(ShoppingCart shoppingCart);

    /**
     * {@code @Description} 查询所有shoppingCart
     * @return shoppingCart集合
     * @param shoppingCart 包含要查询哪个用户的userId
     */
    List<ShoppingCart> selectAll(ShoppingCart shoppingCart);

    /**
     *
     * @param shoppingCart
     */
    void deleteAll(ShoppingCart shoppingCart);
}
