package com.lim.mapper;

import com.lim.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealDishMapper {

    @Select("select * from dish_flavor where id = #{dishId} ")
    DishFlavor selectByDishId(Long dishId);
}
