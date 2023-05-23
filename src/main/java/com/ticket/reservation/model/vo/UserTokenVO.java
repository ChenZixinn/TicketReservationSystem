package com.ticket.reservation.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenVO {
    private int id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String address;
    private String role;
    private String token;
}
