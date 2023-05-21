package com.ticket.reservation.service;

import com.ticket.reservation.model.entity.User;
import com.ticket.reservation.model.request.AddUserReq;

public interface UserService {
    public void register(AddUserReq addUser);

    User login(String username, String password);

    void update(User user);

}
