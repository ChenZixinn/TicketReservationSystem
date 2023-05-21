package com.ticket.reservation.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.entity.User;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class OrderTicketVO implements Serializable {
    private int id;
    private Long ticketId;
    private int userId;
    private String type;
    private String orderStatus;
    private Float totalPrice;
    private String number;
    private String fromCity;
    private String targetCity;
    private Float price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM", locale = "zh", timezone = "GMT+8")
    private Date departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM", locale = "zh", timezone = "GMT+8")
    private Date arrivalTime;
    private int businessClass;
    private int firstClass;
    private int secondClass;
    private String status;
    private String username;
    private String realName;
    private String phone;
    private String address;
}
