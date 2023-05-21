package com.ticket.reservation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.request.SearchTicketReq;
import com.ticket.reservation.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
