package com.lim.mapper;

import com.lim.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface OrderMapper {
    void save(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where id = #{id}")
    void updateStatus(@Param("orderStatus") Integer orderStatus,@Param("orderPaidStatus") Integer orderPaidStatus,@Param("check_out_time") LocalDateTime check_out_time,@Param("id") Long id);
}
