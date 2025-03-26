package com.lim.controller.user;

import com.lim.dto.ShoppingCartDTO;
import com.lim.entity.ShoppingCart;
import com.lim.result.Result;
import com.lim.service.ShopCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShopCartController {

    private final ShopCartService shopCartService;

    public ShopCartController(ShopCartService shopCartService) {
        this.shopCartService = shopCartService;
    }

    @PostMapping("/add")
    public Result save(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shopCartService.save(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result selectAll(){
        List<ShoppingCart> shoppingCartList =shopCartService.selectAll();
        return Result.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    public Result deleteAll(){
        shopCartService.deleteAll();
        return Result.success();
    }
}
