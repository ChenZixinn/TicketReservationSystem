package com.ticket.reservation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.request.SearchTicketReq;

public interface TicketService {
    IPage<Ticket> listTicket(SearchTicketReq productListReq);
}
