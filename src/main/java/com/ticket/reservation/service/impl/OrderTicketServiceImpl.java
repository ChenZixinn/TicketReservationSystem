package com.ticket.reservation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.common.RedisUtils;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.mapper.OrderTicketMapper;
import com.ticket.reservation.mapper.TicketMapper;
import com.ticket.reservation.model.entity.OrderTicket;
import com.ticket.reservation.model.entity.SecurityUser;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.entity.User;
import com.ticket.reservation.model.request.AddOrderTicketReq;
import com.ticket.reservation.model.request.SearchOrderReq;
import com.ticket.reservation.model.vo.OrderTicketVO;
import com.ticket.reservation.service.OrderTicketService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.ticket.reservation.common.Constant.SINGLE_TICKET_CACHE_KEY;

@Service
public class OrderTicketServiceImpl implements OrderTicketService {
    @Autowired
    private OrderTicketMapper orderMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Page<OrderTicketVO> listOrderTicket(SearchOrderReq searchOrderReq) {
        long startTime = System.currentTimeMillis();
        Page<OrderTicketVO> orderTicketVOIPage = null;

        // Spring Security里取出用户id
        SecurityUser secUser = (SecurityUser)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = secUser.getCurrentUserInfo();

        // redis层
        orderTicketVOIPage = redisUtils.getPage(getOrderCacheKey(user.getId()), OrderTicketVO.class);
        if (orderTicketVOIPage!=null){
            System.out.println("order:redis返回, 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
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
        System.out.println("order:数据库返回, 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        return orderTicketVOIPage;
    }


    /**
     * 创建订单
     * @param addOrderTicketReq 创建订单表
     */
    @Override
    public void createOrderTicket(AddOrderTicketReq addOrderTicketReq) {
//        User user = CustomerFilter.currentUser;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((SecurityUser) authentication.getPrincipal()).getCurrentUserInfo();
        if (user == null){
            throw new TicketSystemException(TicketSystemExceptionEnum.NEED_LOGIN);
        }
        Ticket ticket = redisUtils.get(SINGLE_TICKET_CACHE_KEY + addOrderTicketReq.getTicketId(), Ticket.class);
        if (ticket == null){
            ticket = ticketMapper.selectById(addOrderTicketReq.getTicketId());
            redisUtils.set(SINGLE_TICKET_CACHE_KEY + addOrderTicketReq.getTicketId(), JSON.toJSONString(ticket));
        }

        // 先查询是否已经购买过
        QueryWrapper<OrderTicket> query = new QueryWrapper<>();
        query.eq("user_id", user.getId()).eq("ticket_id", addOrderTicketReq.getTicketId()).ne("order_status", "已取消");
        if (orderMapper.selectOne(query) != null){
            throw new TicketSystemException(TicketSystemExceptionEnum.ORDER_EXITS);
        }
        // 判断是否有余票，没余票抛出错误
//        searchTicketCount(addOrderTicketReq);
        RLock lock = redissonClient.getLock(Constant.ORDER_LOCK_KEY + ticket.getId());
        try {
            lock.lock();
            String type = searchTicketCount(addOrderTicketReq);

            OrderTicket orderTicket = new OrderTicket();
            orderTicket.setUserId(user.getId());
            orderTicket.setTicketId(addOrderTicketReq.getTicketId());
            orderTicket.setType(type);
            orderTicket.setOrderStatus("已下单");
            Float price = ticket.getPrice();
            orderTicket.setTotalPrice(price);
            // 更新缓存层
            redisUtils.delete(getOrderCacheKey());
            redisUtils.delete(SINGLE_TICKET_CACHE_KEY + ticket.getId());
            redisUtils.deleteKeysWithPrefix(Constant.TICKET_CACHE_KEY);
            // 更新ticket
            if (addOrderTicketReq.getType().contains("商务座")){
                ticket.setBusinessClass(ticket.getBusinessClass() - 1);
            } else if (addOrderTicketReq.getType().contains( "一等座")) {
                ticket.setFirstClass(ticket.getFirstClass() - 1);
            }else{
                ticket.setSecondClass(ticket.getSecondClass() - 1);
            }
            ticketMapper.updateById(ticket);

            orderMapper.insert(orderTicket);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void cancelOrderTicket(int orderId){

        OrderTicket orderTicket = orderMapper.selectById(orderId);
        if (orderTicket == null){
            throw new TicketSystemException(TicketSystemExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        orderTicket.setOrderStatus("已取消");
        redisUtils.delete(getOrderCacheKey());  // 清除redis 缓存
        redisUtils.deleteKeysWithPrefix(Constant.TICKET_CACHE_KEY);
        orderMapper.updateById(orderTicket);
    }
    public String getOrderCacheKey(){
        User user = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCurrentUserInfo();
        return Constant.ORDER_CACHE_KEY + user.getId();
    }
    public String getOrderCacheKey(int id){
        return Constant.ORDER_CACHE_KEY + id;
    }

    /**
     * 查询是否有余票
     * @param addOrderTicketReq 增加的订单信息
     * @return
     */
    public String searchTicketCount(AddOrderTicketReq addOrderTicketReq){
        String type;

        // 尝试从redis里取
        Ticket ticket = redisUtils.get(SINGLE_TICKET_CACHE_KEY + addOrderTicketReq.getTicketId(), Ticket.class);
        if (ticket == null){
            ticket = ticketMapper.selectById(addOrderTicketReq.getTicketId());
            redisUtils.set(SINGLE_TICKET_CACHE_KEY + addOrderTicketReq.getTicketId(), JSON.toJSONString(ticket));
        }
        // 查询是否有余票
        if (addOrderTicketReq.getType().contains("商务座")){
            type = "商务座";
            if (ticket.getBusinessClass() < 1){
                throw new TicketSystemException(TicketSystemExceptionEnum.TICKET_DONE);
            }
        } else if (addOrderTicketReq.getType().contains("一等座")) {
            type = "一等座";
            if (ticket.getFirstClass() < 1){
                throw new TicketSystemException(TicketSystemExceptionEnum.TICKET_DONE);
            }
        }else{
            type = "二等座";
            if (ticket.getSecondClass() < 1){
                throw new TicketSystemException(TicketSystemExceptionEnum.TICKET_DONE);
            }
        }
        return type;
    }

}
