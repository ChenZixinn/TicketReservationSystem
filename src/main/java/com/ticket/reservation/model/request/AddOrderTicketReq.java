package com.ticket.reservation.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderTicketReq {
//    @ApiModelProperty("用户id")
//    private int userId;

    @ApiModelProperty("车票id")
    private Long ticketId;

    @ApiModelProperty("座位类型，商务座/一等座/二等座")
    private String type;

}
