package com.ticket.reservation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.common.RedisUtils;
import com.ticket.reservation.model.request.AddOrderTicketReq;
import com.ticket.reservation.model.request.SearchOrderReq;
import com.ticket.reservation.model.vo.OrderTicketVO;
import com.ticket.reservation.service.OrderTicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("订单模块")
@CrossOrigin(origins = "*")
public class OrderTicketController {
    @Autowired
    private OrderTicketService orderTicketService;


    @GetMapping("/api/order/list")
    @ApiOperation("订单列表")
    public ApiRestResponse listOrder(SearchOrderReq searchOrderReq){
        return ApiRestResponse.success(orderTicketService.listOrderTicket(searchOrderReq));
    }

    @PostMapping("/api/order/create")
    @ApiOperation("订单创建")
    public ApiRestResponse createOrder(AddOrderTicketReq addOrderTicketReq){
        orderTicketService.createOrderTicket(addOrderTicketReq);
        return ApiRestResponse.success();
    }
    @PostMapping("/api/order/cancel")
    @ApiOperation("订单取消")
    public ApiRestResponse cancelOrder(Integer orderId){
//        orderTicketService.createOrderTicket(addOrderTicketReq);
        orderTicketService.cancelOrderTicket(orderId);
        return ApiRestResponse.success();
    }

}
