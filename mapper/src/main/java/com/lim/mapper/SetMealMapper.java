package com.lim.mapper;

import com.lim.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealMapper {
    @Select("select * from setmeal where id = #{id} ")
    Setmeal selectSetMealByCategoryId(Long id);
}
