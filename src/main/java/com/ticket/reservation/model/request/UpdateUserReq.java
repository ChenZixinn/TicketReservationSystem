package com.ticket.reservation.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserReq {
    @Size(min = 6, max = 16, message = "密码长度为6-16位")
    private String password;
    @Size(min = 2, max = 10, message = "姓名名长度为2-8位")
    private String realName;
    @Size(min = 11, max = 11, message = "手机号码长度错误")
    private String phone;
    @Size(max = 64, message = "地址长度过长")
    private String address;
}
