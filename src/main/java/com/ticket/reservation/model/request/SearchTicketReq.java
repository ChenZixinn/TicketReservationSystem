package com.ticket.reservation.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel("车票实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchTicketReq {
//    @ApiModelProperty(value = "车次")
//    private String number;
    @ApiModelProperty(value = "出发地")
    private String fromCity;

    @ApiModelProperty(value = "目的地")
    private String targetCity;

    @ApiModelProperty(example = "2023-05-10 14:30", value = "出发时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    private Date departureTime;

    @ApiModelProperty(value = "第几页，默认是1")
    private Integer pageNum = 1;
    @ApiModelProperty(value = "每页数量，默认是10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "根据什么排序，默认是时间(departure_time, price)")
    private String orderBy = "departure_time";

    @ApiModelProperty(value = "升序/降序（0/1）")
    private Integer desc = 0;
}
