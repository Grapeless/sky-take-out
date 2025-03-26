package com.lim.mapper;

import com.lim.anno.AutoFill;
import com.lim.entity.Dish;
import com.lim.enumeration.OperationType;
import com.lim.vo.DishVO;
import org.apache.ibatis.annotations.*;

import javax.net.ssl.SSLEngineResult;
import java.util.List;

@Mapper
public interface DishMapper {
    @Select("select * from dish where category_id = #{categoryId}  ")
    List<Dish> selectDishByCategoryId(Long categoryId) ;

    @AutoFill(type = OperationType.INSERT)
    void save(Dish dish);

    List<DishVO> pagingQuery(Dish dish);

    @Select("select * from dish where id = #{id} ")
    Dish selectDishById(Long id);

    void deleteDishByIds(@Param("ids") List<Long> ids);

    @AutoFill(type = OperationType.UPDATE)
    void updateDishById(Dish dish);

    List<Dish> list(Dish dish);
}
