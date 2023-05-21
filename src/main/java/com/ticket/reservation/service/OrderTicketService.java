package com.ticket.reservation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ticket.reservation.model.entity.OrderTicket;
import com.ticket.reservation.model.request.AddOrderTicketReq;
import com.ticket.reservation.model.request.SearchOrderReq;
import com.ticket.reservation.model.vo.OrderTicketVO;

public interface OrderTicketService {

    public IPage<OrderTicketVO> listOrderTicket(SearchOrderReq searchOrderReq);

    void createOrderTicket(AddOrderTicketReq addOrderTicketReq);

    public void cancelOrderTicket(int orderId);
}
