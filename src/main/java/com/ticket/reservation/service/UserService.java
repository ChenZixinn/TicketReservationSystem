package com.ticket.reservation.service;

import com.ticket.reservation.model.entity.User;
import com.ticket.reservation.model.request.AddUserReq;
import com.ticket.reservation.model.request.UpdateUserReq;

public interface UserService {
    public void register(AddUserReq addUser);
    public User getUserInfo();

//    User login(String username, String password);

    void update(UpdateUserReq user);

}
