package com.lim.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lim.entity.DishFlavor;
import com.lim.service.DishService;
import com.lim.constant.MessageConstant;
import com.lim.constant.StatusConstant;
import com.lim.dto.DishDTO;
import com.lim.dto.DishPageQueryDTO;
import com.lim.entity.Dish;
import com.lim.exception.DeletionNotAllowedException;
import com.lim.mapper.DishFlavorMapper;
import com.lim.mapper.DishMapper;
import com.lim.mapper.SetMealDishMapper;
import com.lim.result.PageResult;
import com.lim.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class DishServiceImpl implements DishService {
    private final static String KEY_PREFIX = "Dish:";
    private final DishMapper dishMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final SetMealDishMapper setMealDishMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper, SetMealDishMapper setMealDishMapper, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.dishMapper = dishMapper;
        this.dishFlavorMapper = dishFlavorMapper;
        this.setMealDishMapper = setMealDishMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(DishDTO dishDTO) {
        //先写数据库
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.save(dish);
        //主键返回
        Long dishId = dish.getId();
        dishDTO.getFlavors().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
        dishFlavorMapper.save(dishDTO.getFlavors());
        //再删除缓存
        String key = "KEY_PREFIX" + dishDTO.getCategoryId();
        stringRedisTemplate.delete(key);
    }

    @Override
    public PageResult<DishVO> pagingQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Dish dish = Dish.builder()
                .categoryId(dishPageQueryDTO.getCategoryId())
                .name(dishPageQueryDTO.getName())
                .status(dishPageQueryDTO.getStatus())
                .build();

        Page<DishVO> queryResult = (Page<DishVO>) dishMapper.pagingQuery(dish);

        return new PageResult<>(queryResult.getTotal(), queryResult.getResult());
    }

    @Transactional
    @Override
    public void deleteDish(List<Long> ids) {
        for (Long id : ids) {
            //菜品若处于起售中，则无法删除
            if (dishMapper.selectDishById(id).getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
            //若还有套餐包含该菜品，则无法删除
            if (!setMealDishMapper.selectByDishId(id).isEmpty()) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        //先更新数据库
        dishMapper.deleteDishByIds(ids);
        dishFlavorMapper.deleteDishFlavorByDishIds(ids);
        //再删除缓存
        cacheClean(KEY_PREFIX + "*");
    }

    @Override
    public void updateDishStatus(Integer status, Long id) {
        //还有套餐售卖该菜品，则无法停售
        /*if (!setMealDishMapper.selectByDishId(id).isEmpty()){
            throw new DishEnableFailedException("还有套餐售卖该菜品，则无法停售");
        }*/
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.updateDishById(dish);
        //再删除缓存
        cacheClean(KEY_PREFIX + "*");
    }

    @Override
    public DishVO selectDishById(Long id) {
        Dish dish = dishMapper.selectDishById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavorMapper.selectDishFlavorByDishId(id));
        return dishVO;
    }

    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDishById(dish);

        //先删除全部口味
        List<Long> ids = List.of(dishDTO.getId());
        dishFlavorMapper.deleteDishFlavorByDishIds(ids);

        //新增加的口味不为空，再重新添加口味
        if (!dishDTO.getFlavors().isEmpty()) {
            Long dishId = dish.getId();
            dishDTO.getFlavors().forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.save(dishDTO.getFlavors());
        }
        //再删除缓存
        cacheClean(KEY_PREFIX + "*");
    }

    @Override
    public List<Dish> selectDishByCategoryId(Long categoryId) {
        return dishMapper.selectDishByCategoryId(categoryId);
    }

    public List<DishVO> listWithFlavor(Dish dish) {
        String key = KEY_PREFIX + dish.getCategoryId();
        //1.先从redis查询
        String dishVOJson = stringRedisTemplate.opsForValue().get(key);
        //2.判断查询结果
        if (StringUtils.hasText(dishVOJson)) {
            //存在,反序列化后直接返回查询结果
            try {
                return objectMapper.readValue(dishVOJson, new TypeReference<List<DishVO>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //3.不存在,查询数据库(这里未判断数据库查询结果是否为空)
        List<Dish> dishList = dishMapper.list(dish);
        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectDishFlavorByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        //4.写入redis
        try {
            stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(dishVOList), 30L, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //5.返回
        return dishVOList;
    }

    private void cacheClean(String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        stringRedisTemplate.delete(keys);
    }
}
