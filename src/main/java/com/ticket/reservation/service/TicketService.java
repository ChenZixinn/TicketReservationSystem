package com.ticket.reservation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.request.AddOrderTicketReq;
import com.ticket.reservation.model.request.AddTicket;
import com.ticket.reservation.model.request.SearchTicketReq;

public interface TicketService {
    IPage<Ticket> listTicket(SearchTicketReq productListReq);

    // todo 创建
    void createTicket(AddTicket addTicket);

    // todo  删除
    public void cancelTicket(int orderId);

    // todo 修改
    int updateTicket(AddTicket addTicket);
}
