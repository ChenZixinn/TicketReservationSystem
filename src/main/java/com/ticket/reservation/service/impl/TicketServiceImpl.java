package com.ticket.reservation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.common.RedisUtils;
import com.ticket.reservation.mapper.TicketMapper;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.request.SearchTicketReq;
import com.ticket.reservation.service.TicketService;
import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.PrintConversionEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private RedisUtils redisUtils;


    @Override
    public IPage<Ticket> listTicket(SearchTicketReq searchTicketReq) {
        Page<Ticket> ticketPage = null;

        // redis层
        ticketPage = redisUtils.getPage(Constant.TICKET_CACHE_KEY + searchTicketReq, Ticket.class);
        if (ticketPage!=null){
            System.out.println("redis返回");
            return ticketPage;
        }

        // mysql层
        Page<Ticket> page = new Page<>(searchTicketReq.getPageNum(), searchTicketReq.getPageSize());

        // 创建查询条件构造器
        QueryWrapper<Ticket> queryWrapper = new QueryWrapper<>();
        Date today = new Date();

        // 日期查询
        if (searchTicketReq.getDepartureTime() != null){
            if (searchTicketReq.getDepartureTime().after(today)){
                queryWrapper.gt("departure_time", searchTicketReq.getDepartureTime());
            }else{
                queryWrapper.gt("departure_time", today);
            }
        }else{
            queryWrapper.gt("departure_time", today);
        }

        // 排序
        if (searchTicketReq.getOrderBy() != null){
            if (searchTicketReq.getDesc() == 1){
                queryWrapper.orderByDesc(searchTicketReq.getOrderBy());
            }else {
                queryWrapper.orderByAsc(searchTicketReq.getOrderBy());
            }
        }

        // 出发地
        if (searchTicketReq.getFromCity() != null){
            queryWrapper.eq("from_city", searchTicketReq.getFromCity());
        }

        // 目的地
        if (searchTicketReq.getTargetCity() != null){
            queryWrapper.eq("target_city", searchTicketReq.getTargetCity());
        }
        ticketPage = ticketMapper.selectPage(page, queryWrapper);

        // 写入redis
        redisUtils.set(Constant.TICKET_CACHE_KEY + searchTicketReq, JSON.toJSONString(ticketPage), 1, TimeUnit.HOURS);
        return ticketPage;
    }
}
