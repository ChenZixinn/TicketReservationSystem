package com.ticket.reservation.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ticket.reservation.model.entity.OrderTicket;
import com.ticket.reservation.model.vo.OrderTicketVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface OrderTicketMapper extends BaseMapper<OrderTicket> {
    @Select("SELECT o.*, t.*, u.* FROM `order_ticket` o LEFT JOIN ticket t ON o.ticket_id = t.id LEFT JOIN user u ON o.user_id = u.id WHERE o.id = #{id} and u.id = #{userId}")
    IPage<OrderTicket> selectOrderWithTicketAndUserById(Page<OrderTicket> page,@Param("id") int id, @Param("userId") int userId);
    @Select("SELECT o.*, t.*, u.* FROM `order_ticket` o LEFT JOIN ticket t ON o.ticket_id = t.id LEFT JOIN user u ON o.user_id = u.id WHERE u.id = #{userId} and o.order_status = '已下单'")
    Page<OrderTicketVO> selectOrderWithTicketAndUser(Page<OrderTicket> page, @Param("userId") int userId, QueryWrapper<OrderTicket> queryWrapper);
}
