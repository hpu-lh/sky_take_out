package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    void insertOrder(Orders orders);

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

    Page<OrderVO> selectOrdersForPageByUserId(OrdersPageQueryDTO ordersPageQueryDTO);

    Orders selectOrderById(Integer id);

    void updateOrdersStatusAsCancel(Integer id);

    Integer selectCountOfToBeConfirmedOrder(Integer status);

    Integer selectCountOfConfirmedOrder(Integer status);

    Integer selectCountOfDeliveryInProgressOrder(Integer status);

    List<Orders> selectUnpayedAndTimeout0rderList(@Param("status") Integer status, @Param("localDateTime") LocalDateTime localDateTime);

    Double selectCountOfDate(Map map);

    Integer selectCountOfOrder(Map map);

    List<GoodsSalesDTO> selectSalesTop10(@Param("begin") LocalDateTime begin,@Param("end") LocalDateTime end);
}
