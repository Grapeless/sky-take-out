package com.lim.mapper;

import com.lim.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void save(@Param("orderDetailList") List<OrderDetail> orderDetailList);
}
