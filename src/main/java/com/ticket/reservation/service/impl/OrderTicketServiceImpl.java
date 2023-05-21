package com.ticket.reservation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.common.RedisUtils;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.mapper.OrderTicketMapper;
import com.ticket.reservation.mapper.TicketMapper;
import com.ticket.reservation.model.entity.OrderTicket;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.entity.User;
import com.ticket.reservation.model.request.AddOrderTicketReq;
import com.ticket.reservation.model.request.SearchOrderReq;
import com.ticket.reservation.model.vo.OrderTicketVO;
import com.ticket.reservation.service.OrderTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import java.util.concurrent.TimeUnit;

@Service
public class OrderTicketServiceImpl implements OrderTicketService {
    @Autowired
    private OrderTicketMapper orderMapper;

    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Page<OrderTicketVO> listOrderTicket(SearchOrderReq searchOrderReq) {
        Page<OrderTicketVO> orderTicketVOIPage = null;
        User user = CustomerFilter.currentUser;
        if (user == null){
            throw new TicketSystemException(TicketSystemExceptionEnum.NEED_LOGIN);
        }

        // redis层
        orderTicketVOIPage = redisUtils.get(getOrderCacheKey(), Page.class);
        if (orderTicketVOIPage!=null){
            System.out.println("redis返回");
            return orderTicketVOIPage;
        }

        // 数据库层
        Page<OrderTicket> page = new Page<>(searchOrderReq.getPageNum(), searchOrderReq.getPageSize());
        QueryWrapper<OrderTicket> query = new QueryWrapper<>();

        if (searchOrderReq.getOrderId() != null){
            query.eq("id", searchOrderReq.getOrderId());
        }
        query.eq("order_status", "已下单");
        orderTicketVOIPage = orderMapper.selectOrderWithTicketAndUser(page, user.getId(), query);

        redisUtils.set(getOrderCacheKey(), JSON.toJSONString(orderTicketVOIPage), 1, TimeUnit.HOURS);
        return orderTicketVOIPage;
    }

    @Override
    public void createOrderTicket(AddOrderTicketReq addOrderTicketReq) {
        User user = CustomerFilter.currentUser;
        if (user == null){
            throw new TicketSystemException(TicketSystemExceptionEnum.NEED_LOGIN);
        }
        // 先查询是否已经购买过
        QueryWrapper<OrderTicket> query = new QueryWrapper<>();
        query.eq("user_id", user.getId()).eq("ticket_id", addOrderTicketReq.getTicketId()).ne("order_status", "已取消");
        if (orderMapper.selectOne(query) != null){
            throw new TicketSystemException(TicketSystemExceptionEnum.ORDER_EXITS);
        }

        OrderTicket orderTicket = new OrderTicket();
        orderTicket.setUserId(user.getId());
        orderTicket.setTicketId(addOrderTicketReq.getTicketId());
        orderTicket.setType(addOrderTicketReq.getType());
        orderTicket.setOrderStatus("已下单");
        Float price = ticketMapper.selectById(addOrderTicketReq.getTicketId()).getPrice();
        orderTicket.setTotalPrice(price);
        redisUtils.delete(getOrderCacheKey());
        orderMapper.insert(orderTicket);
    }

    @Override
    public void cancelOrderTicket(int orderId){
        OrderTicket orderTicket = orderMapper.selectById(orderId);
        if (orderTicket == null){
            throw new TicketSystemException(TicketSystemExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        orderTicket.setOrderStatus("已取消");
        redisUtils.delete(getOrderCacheKey());
        orderMapper.updateById(orderTicket);
    }

    public String getOrderCacheKey(){
        return Constant.ORDER_CACHE_KEY + CustomerFilter.currentUser.getId();
    }
}
