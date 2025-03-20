package com.lim.Service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lim.Service.SetMealService;
import com.lim.constant.MessageConstant;
import com.lim.dto.SetmealDTO;
import com.lim.dto.SetmealPageQueryDTO;
import com.lim.entity.Setmeal;
import com.lim.entity.SetmealDish;
import com.lim.exception.DeletionNotAllowedException;
import com.lim.mapper.DishMapper;
import com.lim.mapper.SetMealDishMapper;
import com.lim.mapper.SetMealMapper;
import com.lim.result.PageResult;
import com.lim.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    private final SetMealMapper setMealMapper;
    private final SetMealDishMapper setMealDishMapper;
    private final DishMapper dishMapper;

    public SetMealServiceImpl(SetMealMapper setMealMapper, SetMealDishMapper setMealDishMapper, DishMapper dishMapper) {
        this.setMealMapper = setMealMapper;
        this.setMealDishMapper = setMealDishMapper;
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult<SetmealVO> pagingQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        //拷贝一个entity对象方便查询
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealPageQueryDTO, setmeal);

        Page<SetmealVO> queryResult = (Page<SetmealVO>) setMealMapper.pagingQuery(setmeal);

        return new PageResult<>(queryResult.getTotal(), queryResult.getResult());
    }

    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.save(setmeal);

        Long setMealId = setmeal.getId();
        setmealDTO.getSetmealDishes().forEach(setmealDish -> {
            setmealDish.setSetmealId(setMealId);
        });
        setMealDishMapper.save(setmealDTO.getSetmealDishes());
    }

    @Override
    public SetmealVO selectSetMealVOById(Long id) {
        SetmealVO setmealVO = setMealMapper.selectSetMealVOById(id);
        List<SetmealDish> setMealDish = setMealDishMapper.selectBySetMealId(id);
        setmealVO.setSetmealDishes(setMealDish);
        return setmealVO;
    }

    @Transactional
    @Override
    public void updateSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.updateSetMeal(setmeal);

        //先删除全部已关联的菜品
        List<Long> id = List.of(setmealDTO.getId());
        setMealDishMapper.deleteSetMealDishBySetMealIds(id);
        //再重新添加
        setmealDTO.getSetmealDishes().forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
        setMealDishMapper.save(setmealDTO.getSetmealDishes());

    }

    @Override
    public void updateSetMealStatus(Integer status, Long id) {
        //若套餐内包含未启售菜品，则无法启售
        //只切换为启售状态时
        if (status == 1) {
            //先查套餐所含所有菜品
            setMealDishMapper.selectBySetMealId(id).forEach(setmealDish -> {
                //再检查每个菜品的status
                if (dishMapper.selectDishById(setmealDish.getDishId()).getStatus() == 0) {
                    throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setMealMapper.updateSetMeal(setmeal);
    }

    @Transactional
    @Override
    public void deleteSetMeal(List<Long> ids) {
        //套餐若处于起售中，则无法删除
        setMealMapper.selectSetMealByIds(ids).forEach(setmeal -> {
            if (setmeal.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        setMealMapper.deleteSetMealBySetMealIds(ids);
        setMealDishMapper.deleteSetMealDishBySetMealIds(ids);
    }
}
