package com.lim.service.impl;

import com.lim.service.ShopService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ShopServiceImpl implements ShopService {
    private final static String KEY = "SHOP_STATUS";
    private final StringRedisTemplate stringRedisTemplate;

    public ShopServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void updateShopStatus(Integer status) {
        stringRedisTemplate.opsForValue().set(KEY,status.toString());
    }

    @Override
    public Integer queryShopStatus() {
       return Integer.valueOf(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(KEY)));
    }
}
