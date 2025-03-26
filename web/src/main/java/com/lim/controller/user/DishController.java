package com.lim.controller.user;

import com.lim.constant.StatusConstant;
import com.lim.entity.Dish;
import com.lim.result.Result;
import com.lim.service.DishService;
import com.lim.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        //查询起售中的菜品
        List<DishVO> list = dishService.listWithFlavor(dish);

        return Result.success(list);
    }

}
