package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertOrderDetailByList(List<OrderDetail> orderDetailList);

    List<OrderDetail> selectOrderDetailForPageByOrderId(Long orderId);

    List<OrderDetail> selectOrderDetailList(Integer orderId);

}
