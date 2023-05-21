package com.ticket.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.request.SearchTicketReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {


}
