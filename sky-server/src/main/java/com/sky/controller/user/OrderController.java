package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端-订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception{
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        OrderPaymentVO orderPaymentVO = new OrderPaymentVO();
        orderPaymentVO.setNonceStr(LocalDateTime.now().toString());
        orderPaymentVO.setPackageStr("string");
        orderPaymentVO.setPaySign("string");
        orderPaymentVO.setSignType("RSA");
        orderPaymentVO.setTimeStamp("string");
        return Result.success(orderPaymentVO);
    }
//    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        log.info("订单支付：{}", ordersPaymentDTO);
//        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);
//        return Result.success(orderPaymentVO);
//    }
    @GetMapping("/historyOrders")
    @ApiOperation("查询历史订单")
    public Result<PageResult> queryHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("查询历史订单：{}",ordersPageQueryDTO);
        PageResult pageResult = orderService.queryHistoryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result queryHistoryOrdersDetail(@PathVariable Integer id){
        log.info("查询订单详情：{}",id);
        OrderVO orderVO = orderService.selectOrderDetailById(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancelOrder(@PathVariable Integer id){
        log.info("取消订单：{}",id);
        Result result=null;
        try {
            result = orderService.cancelOrder(id);
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
        return result;
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repeateOrder(@PathVariable Integer id){
        log.info("再来一单：{}",id);
        Result result = orderService.repeateOrder(id);
        return result;
    }

    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id){
        Result result = orderService.reminder(id);
        return result;
    }

}
