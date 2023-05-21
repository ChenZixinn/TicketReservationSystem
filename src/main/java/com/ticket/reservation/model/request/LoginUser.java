package com.ticket.reservation.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginUser {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
