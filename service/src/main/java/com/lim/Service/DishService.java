package com.lim.Service;

import com.lim.dto.DishDTO;
import com.lim.dto.DishPageQueryDTO;
import com.lim.result.PageResult;
import com.lim.vo.DishVO;

public interface DishService {
    void save(DishDTO dishDTO);

    PageResult<DishVO> pagingQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(Long[] ids);

    void updateDishStatus(Integer status,Long id);

    DishVO selectDishById(Long id);

    void updateDish(DishDTO dishDTO);
}
