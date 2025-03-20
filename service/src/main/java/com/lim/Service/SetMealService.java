package com.lim.Service;

import com.lim.dto.SetmealDTO;
import com.lim.dto.SetmealPageQueryDTO;
import com.lim.result.PageResult;
import com.lim.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    PageResult<SetmealVO> pagingQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(SetmealDTO setmealDTO);

    SetmealVO selectSetMealVOById(Long id);

    void updateSetMeal(SetmealDTO setmealDTO);

    void updateSetMealStatus(Integer status, Long id);

    void deleteSetMeal(List<Long> ids);
}
