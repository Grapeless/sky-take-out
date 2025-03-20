package com.lim.mapper;

import com.lim.anno.AutoFill;
import com.lim.entity.DishFlavor;
import com.lim.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void save(@Param("dishFlavors") List<DishFlavor> dishFlavors);

    @Select("select * from dish_flavor where dish_id = #{dishId} ")
    List<DishFlavor> selectDishFlavorByDishId(Long dishId);

    void deleteDishFlavorByDishIds(@Param("dishIds") List<Long> dishIds);

}
