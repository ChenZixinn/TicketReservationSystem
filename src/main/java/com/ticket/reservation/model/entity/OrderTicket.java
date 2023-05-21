package com.ticket.reservation.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_ticket")
public class OrderTicket implements Serializable {
    private int id;

    @TableField("ticket_id")
    private Long ticketId;

    @TableField(exist = false)
    private Ticket ticket;

    @TableField(value = "user_id")
    private int userId;

    @TableField(exist = false)
    private User user;

    private String type;

    private Float totalPrice;

    private String orderStatus;
}
