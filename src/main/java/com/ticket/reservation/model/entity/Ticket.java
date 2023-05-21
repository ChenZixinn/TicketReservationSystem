package com.ticket.reservation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ticket")
public class Ticket implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
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
}
