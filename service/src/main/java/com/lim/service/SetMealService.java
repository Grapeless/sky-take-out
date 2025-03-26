package com.lim.service;

import com.lim.dto.SetmealDTO;
import com.lim.dto.SetmealPageQueryDTO;
import com.lim.entity.Setmeal;
import com.lim.result.PageResult;
import com.lim.vo.DishItemVO;
import com.lim.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    PageResult<SetmealVO> pagingQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(SetmealDTO setmealDTO);

    SetmealVO selectSetMealVOById(Long id);

    void updateSetMeal(SetmealDTO setmealDTO);

    void updateSetMealStatus(Integer status, Long id);

    void deleteSetMeal(List<Long> ids);

    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> getDishItemById(Long id);
}
