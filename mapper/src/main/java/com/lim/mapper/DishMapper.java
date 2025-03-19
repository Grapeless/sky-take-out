package com.lim.mapper;

import com.lim.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    @Select("select * from dish where id = #{id} ")
    Dish selectDishByCategoryId(Long id) ;
}
