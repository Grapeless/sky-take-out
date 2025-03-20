package com.lim.Service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lim.Service.DishService;
import com.lim.constant.MessageConstant;
import com.lim.constant.StatusConstant;
import com.lim.dto.DishDTO;
import com.lim.dto.DishPageQueryDTO;
import com.lim.entity.Dish;
import com.lim.entity.DishFlavor;
import com.lim.exception.DeletionNotAllowedException;
import com.lim.exception.DishEnableFailedException;
import com.lim.mapper.DishFlavorMapper;
import com.lim.mapper.DishMapper;
import com.lim.mapper.SetMealDishMapper;
import com.lim.mapper.SetMealMapper;
import com.lim.result.PageResult;
import com.lim.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final SetMealDishMapper setMealDishMapper;

    public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper, SetMealDishMapper setMealDishMapper) {
        this.dishMapper = dishMapper;
        this.dishFlavorMapper = dishFlavorMapper;
        this.setMealDishMapper = setMealDishMapper;
    }

    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.save(dish);
        //主键返回
        Long dishId = dish.getId();
        dishDTO.getFlavors().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
        dishFlavorMapper.save(dishDTO.getFlavors());
    }

    @Override
    public PageResult<DishVO> pagingQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Dish dish = Dish.builder()
                .categoryId(dishPageQueryDTO.getCategoryId())
                .name(dishPageQueryDTO.getName())
                .status(dishPageQueryDTO.getStatus())
                .build();

        Page<DishVO> queryResult = (Page<DishVO>) dishMapper.pagingQuery(dish);

        return new PageResult<>(queryResult.getTotal(),queryResult.getResult());
    }

    @Transactional
    @Override
    public void deleteDish(List<Long> ids) {
        for (Long id : ids) {
            //菜品若处于起售中，则无法删除
            if(dishMapper.selectDishById(id).getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
            //若还有套餐包含该菜品，则无法删除
            if (!setMealDishMapper.selectByDishId(id).isEmpty()){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        dishMapper.deleteDishByIds(ids);
        dishFlavorMapper.deleteDishFlavorByDishIds(ids);
    }

    @Override
    public void updateDishStatus(Integer status,Long id) {
        //还有套餐售卖该菜品，则无法停售
        /*if (!setMealDishMapper.selectByDishId(id).isEmpty()){
            throw new DishEnableFailedException("还有套餐售卖该菜品，则无法停售");
        }*/
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.updateDishById(dish);
    }

    @Override
    public DishVO selectDishById(Long id) {
        Dish dish = dishMapper.selectDishById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavorMapper.selectDishFlavorByDishId(id));
        return dishVO;
    }

    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDishById(dish);

        //先删除全部口味
        List<Long> ids = List.of(dishDTO.getId());
        dishFlavorMapper.deleteDishFlavorByDishIds(ids);

        //再重新添加口味
        Long dishId = dish.getId();
        dishDTO.getFlavors().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
        dishFlavorMapper.save(dishDTO.getFlavors());
    }

    @Override
    public List<Dish> selectDishByCategoryId(Long categoryId) {
        return dishMapper.selectDishByCategoryId(categoryId);
    }
}
