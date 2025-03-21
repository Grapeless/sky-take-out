package com.lim.controller.admin;

import com.lim.Service.SetMealService;
import com.lim.dto.SetmealDTO;
import com.lim.dto.SetmealPageQueryDTO;
import com.lim.result.PageResult;
import com.lim.result.Result;
import com.lim.vo.SetmealVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
public class SetMealController {

    private final SetMealService setMealService;

    public SetMealController(SetMealService setMealService) {
        this.setMealService = setMealService;
    }

    @GetMapping("/page")
    public Result pagingQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult<SetmealVO> queryResult = setMealService.pagingQuery(setmealPageQueryDTO);
        return Result.success(queryResult);
    }

    @PostMapping()
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setMealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result selectSetMealVOById(@PathVariable Long id){
        SetmealVO setmealVO = setMealService.selectSetMealVOById(id);
        return Result.success(setmealVO);
    }

     @PutMapping()
    public Result updateSetMeal(@RequestBody SetmealDTO setmealDTO){
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result updateSetMealStatus(@PathVariable Integer status,Long id){
        setMealService.updateSetMealStatus(status,id);
        return Result.success();
    }

    @DeleteMapping()
    public Result delete(@RequestParam List<Long> ids){
        setMealService.deleteSetMeal(ids);
        return Result.success();
    }
}
