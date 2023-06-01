package com.ticket.reservation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.request.AddOrderTicketReq;
import com.ticket.reservation.model.request.AddTicket;
import com.ticket.reservation.model.request.SearchTicketReq;
import com.ticket.reservation.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api("车票管理")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @ApiOperation("查询车票列表")
    @GetMapping("/api/ticket/list")
    public ApiRestResponse<IPage<Ticket>> listTicket(SearchTicketReq searchTicketReq){
        System.out.println(searchTicketReq);
        IPage<Ticket> ticketIPage = ticketService.listTicket(searchTicketReq);
        return ApiRestResponse.success(ticketIPage);
    }

    // todo 添加机票controller
    @ApiOperation("添加机票")
    @PostMapping("/api/ticket/create")
    public ApiRestResponse createOrder(AddTicket addTicket){
        ticketService.createTicket(addTicket);
        return ApiRestResponse.success();
    }

    // 删除机票 controller
    @PostMapping("/api/ticket/cancel")
    @ApiOperation("删除机票")
    public ApiRestResponse cancelOrder(Integer ticketId){
    //        orderTicketService.createOrderTicket(addOrderTicketReq);
        ticketService.cancelTicket(ticketId);
        return ApiRestResponse.success();
    }

    // 修改机票 controller
    @PostMapping("/api/ticket/update")
    @ApiOperation("更新机票")
    public ApiRestResponse updateTicket(AddTicket addTicket){
        System.out.println(addTicket);

        ticketService.updateTicket(addTicket);
        return ApiRestResponse.success();
    }


}
