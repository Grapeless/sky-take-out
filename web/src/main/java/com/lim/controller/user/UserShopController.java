package com.lim.controller.user;

import com.lim.Service.ShopService;
import com.lim.result.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shop")
public class UserShopController {
    private final ShopService shopService;

    public UserShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/status")
    public Result queryShopStatus(){
        Integer status = shopService.queryShopStatus();
        return Result.success(status);
    }
}
