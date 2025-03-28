package com.lim.mapper;

import com.lim.anno.AutoFill;
import com.lim.entity.Setmeal;
import com.lim.enumeration.OperationType;
import com.lim.vo.DishItemVO;
import com.lim.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {
    @Select("select * from setmeal where category_id = #{categoryId}  ")
    Setmeal selectSetMealByCategoryId(Long categoryId);

    List<SetmealVO> pagingQuery(Setmeal setmeal);

    @AutoFill(type = OperationType.INSERT)
    void save(Setmeal setmeal);

    SetmealVO selectSetMealVOById(Long id);

    @AutoFill(type = OperationType.UPDATE)
    void updateSetMeal(Setmeal setmeal);

    List<Setmeal> selectSetMealByIds(@Param("ids") List<Long> ids);

    void deleteSetMealBySetMealIds(@Param("setMealIds")List<Long> setMealIds);

    List<Setmeal> list(Setmeal setmeal);

    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setMealId}")
    List<DishItemVO> getDishItemBySetMealId(Long setMealId);
}
