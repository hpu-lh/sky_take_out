package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.core.annotation.Order;

public interface OrderService {

    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    PageResult queryHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO selectOrderDetailById(Integer id);

    Result cancelOrder(Integer id) throws Exception;

    Result repeateOrder(Integer id);

    PageResult selectOrderListForPage(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO queryCountOfDiffStatus();

    Result rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    Result confirmOrder(OrdersConfirmDTO ordersConfirmDTO);
    Result cancelOrder(OrdersCancelDTO ordersCancelDTO);

    Result deliveryOrder(Long id);

    Result compeleteOrder(Long id);

    Result reminder(Long id);
}
