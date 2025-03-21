package com.lim.controller.user;

import com.lim.Service.ShopService;
import com.lim.result.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/shop")
public class UserShopController {
    private final ShopService shopService;

    public UserShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PutMapping("/{status}")
    public Result updateShopStatus(@PathVariable Integer status){
        shopService.updateShopStatus(status);
        return Result.success();
    }
}
