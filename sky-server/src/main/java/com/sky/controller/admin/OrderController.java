package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.selectOrderListForPage(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("各订单数量统计")
    public Result queryCountOfOrder() {
        OrderStatisticsVO orderStatisticsVO = orderService.queryCountOfDiffStatus();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("查看订单详情")
    public Result queryOrderDetail(@PathVariable Integer id) {
        OrderVO orderVO = orderService.selectOrderDetailById(id);
        return Result.success(orderVO);
    }

    @PutMapping("/rejection")
    @ApiOperation("拒绝订单")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        Result result = orderService.rejectOrder(ordersRejectionDTO);
        return result;
    }

    @PutMapping("/confirm")
    @ApiOperation("接收订单")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        Result result = orderService.confirmOrder(ordersConfirmDTO);
        return result;
    }
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        Result result = orderService.cancelOrder(ordersCancelDTO);
        return result;
    }
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result deliveryOrder(@PathVariable Long id) {
        Result result = orderService.deliveryOrder(id);
        return result;
    }
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result compeleteOrder(@PathVariable Long id) {
        Result result = orderService.compeleteOrder(id);
        return result;
    }
}
