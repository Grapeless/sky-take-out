package com.lim.controller.admin;

import com.lim.service.DishService;
import com.lim.dto.DishDTO;
import com.lim.dto.DishPageQueryDTO;
import com.lim.entity.Dish;
import com.lim.result.PageResult;
import com.lim.result.Result;
import com.lim.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.save(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result pagingQuery(DishPageQueryDTO dishPageQueryDTO){
        PageResult<DishVO> queryResult = dishService.pagingQuery(dishPageQueryDTO);
        return Result.success(queryResult);
    }

    @DeleteMapping()
    public Result deleteDish(@RequestParam List<Long> ids){
        dishService.deleteDish(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result updateDishStatus(@PathVariable Integer status,Long id){
        dishService.updateDishStatus(status,id);
        return Result.success();

    }

    @GetMapping("/{id}")
    public Result selectDishById(@PathVariable Long id){
        DishVO queryResult = dishService.selectDishById(id);
        return Result.success(queryResult);
    }

    @PutMapping()
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result selectDishByCategoryId(Long categoryId){
        List<Dish> dishes = dishService.selectDishByCategoryId(categoryId);
        return Result.success(dishes);
    }
}
