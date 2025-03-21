package com.lim.controller.admin;

import com.lim.Service.ShopService;
import com.lim.result.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
public class AdminShopController {
    private final ShopService shopService;

    public AdminShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PutMapping("/{status}")
    public Result updateShopStatus(@PathVariable Integer status){
        shopService.updateShopStatus(status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result queryShopStatus(){
        Integer status = shopService.queryShopStatus();
        return Result.success(status);
    }
}
