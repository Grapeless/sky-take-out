package com.lim.controller.user;

import com.lim.dto.OrdersPaymentDTO;
import com.lim.dto.OrdersSubmitDTO;
import com.lim.result.Result;
import com.lim.service.OrderService;
import com.lim.vo.OrderPaymentVO;
import com.lim.vo.OrderSubmitVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
public class UserOrderController {
    private final OrderService orderService;

    public UserOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    public Result submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO submitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(submitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    public Result payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        return Result.success(orderPaymentVO);
    }

    @PostMapping("/repetition/{id}")
    public Result onceMore(@PathVariable Long id){
        return Result.success();
    }
}
