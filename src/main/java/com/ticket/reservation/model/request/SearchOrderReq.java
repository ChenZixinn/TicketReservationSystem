package com.ticket.reservation.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchOrderReq {

    @ApiModelProperty(value = "订单编号,可以为空")
    Integer orderId;
    @ApiModelProperty(value = "第几页，默认是1")
    private Integer pageNum = 1;
    @ApiModelProperty(value = "每页数量，默认是10")
    private Integer pageSize = 10;
}
