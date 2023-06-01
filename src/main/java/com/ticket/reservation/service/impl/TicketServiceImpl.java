package com.ticket.reservation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.common.RedisUtils;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.mapper.TicketMapper;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.entity.User;
import com.ticket.reservation.model.request.AddOrderTicketReq;
import com.ticket.reservation.model.request.AddTicket;
import com.ticket.reservation.model.request.SearchTicketReq;
import com.ticket.reservation.service.TicketService;
import org.assertj.core.util.DateUtil;
import org.springframework.beans.BeanUtils;
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

    private SearchTicketReq cache; //用户储存拼接 key

    public String getTicketCacheKey(){
        return Constant.TICKET_CACHE_KEY + cache;
    }

    @Override
    public IPage<Ticket> listTicket(SearchTicketReq searchTicketReq) {
        Page<Ticket> ticketPage = null;
        cache = searchTicketReq;
        // redis层
        ticketPage = redisUtils.getPage(getTicketCacheKey(), Ticket.class);
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

    @Override
    public void createTicket(AddTicket addTicket) {
        //todo 创建的的业务
        // 创建机票
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(addTicket,ticket);

        int insert = ticketMapper.insert(ticket);
        redisUtils.delete(getTicketCacheKey());  // 清除redis 缓存
    }

    @Override
    public void cancelTicket(int ticketId) {
        //todo 删除的业务
        System.out.println("你要删除的id是"+ticketId);

        redisUtils.delete(getTicketCacheKey());  // 清除redis 缓存
        //DELETE from ticket where id = 53
        ticketMapper.deleteById(ticketId);
    }

    @Override
    public int updateTicket(AddTicket addTicket) {

        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(addTicket,ticket);
        System.out.println("修改"+addTicket);
        int i = ticketMapper.updateById(ticket);
        redisUtils.delete(getTicketCacheKey());  // 清除redis 缓存
        return 1;
    }
}
