package com.lim.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lim.constant.MessageConstant;
import com.lim.context.BaseContext;
import com.lim.dto.OrdersPaymentDTO;
import com.lim.dto.OrdersSubmitDTO;
import com.lim.entity.*;
import com.lim.exception.AddressBookBusinessException;
import com.lim.exception.ShoppingCartBusinessException;
import com.lim.mapper.OrderDetailMapper;
import com.lim.mapper.OrderMapper;
import com.lim.mapper.ShopCartMapper;
import com.lim.mapper.UserMapper;
import com.lim.service.AddressBookService;
import com.lim.service.OrderService;
import com.lim.utils.WeChatPayUtil;
import com.lim.vo.OrderPaymentVO;
import com.lim.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final AddressBookService addressBookService;
    private final ShopCartMapper shopCartMapper;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final WeChatPayUtil weChatPayUtil;
    private final UserMapper userMapper;
    private Long orderId;

    public OrderServiceImpl(AddressBookService addressBookService, ShopCartMapper shopCartMapper, OrderMapper orderMapper, OrderDetailMapper orderDetailMapper, WeChatPayUtil weChatPayUtil, UserMapper userMapper) {
        this.addressBookService = addressBookService;
        this.shopCartMapper = shopCartMapper;
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.weChatPayUtil = weChatPayUtil;
        this.userMapper = userMapper;
    }


    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //1.处理空值异常
        //地址为空
        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //购物车为空
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> shoppingCarts = shopCartMapper.selectShoppingCart(shoppingCart);
        if (shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //2.插入订单表
        Orders orders = Orders.builder()
                .orderTime(LocalDateTime.now())
                .payStatus(Orders.UN_PAID)
                .status(Orders.PENDING_PAYMENT)
                .number(String.valueOf(System.currentTimeMillis()))
                .phone(addressBook.getPhone())
                .consignee(addressBook.getConsignee())
                .userId(BaseContext.getCurrentId())
                .build();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orderMapper.save(orders);
        orderId = orders.getId();
        //3.批量插入订单明细表
        List<OrderDetail> orderDetailList = new ArrayList<>();
        shoppingCarts.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.save(orderDetailList);

        //4.清空购物车
        shopCartMapper.deleteByUserId(BaseContext.getCurrentId());
        //5.返回VO
        return OrderSubmitVO.builder()
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .build();
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        /*JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }*/


        //为替代微信支付成功后的数据库订单状态更新，多定义一个方法进行修改
        Integer OrderPaidStatus = Orders.PAID; //支付状态，已支付
        Integer OrderStatus = Orders.TO_BE_CONFIRMED;  //订单状态，待接单
        //发现没有将支付时间 check_out属性赋值，所以在这里更新
        LocalDateTime check_out_time = LocalDateTime.now();
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    @Override
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

}
