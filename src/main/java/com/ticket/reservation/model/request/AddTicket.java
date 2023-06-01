package com.ticket.reservation.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTicket {
        @TableId(value = "id", type = IdType.AUTO)
        private Long id;
        private String number;
        private String fromCity;
        private String targetCity;
        private Float price;

        @ApiModelProperty(example = "2023-05-10 14:30", value = "出发时间")
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
        //@JsonFormat(pattern = "yyyy-MM-dd HH:MM", locale = "zh", timezone = "GMT+8")
        private Date departureTime;

        @ApiModelProperty(example = "2023-05-10 14:30", value = "到达时间")
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
        //@JsonFormat(pattern = "yyyy-MM-dd HH:MM", locale = "zh", timezone = "GMT+8")
        private Date arrivalTime;
        private int businessClass;
        private int firstClass;
        private int secondClass;
        private String status;
}
