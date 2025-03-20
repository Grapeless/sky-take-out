package com.lim.mapper;

import com.lim.anno.AutoFill;
import com.lim.entity.DishFlavor;
import com.lim.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    @Select("select * from setmeal_dish where dish_id = #{dishId} ")
    List<SetmealDish> selectByDishId(Long dishId);

    void save(@Param("setMealDishes") List<SetmealDish> setMealDishes);

    List<SetmealDish> selectBySetMealId(Long setMealId);

    void deleteSetMealDishBySetMealIds(@Param("setMealIds") List<Long> setMealIds);
}
