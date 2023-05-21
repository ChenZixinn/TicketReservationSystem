package com.ticket.reservation.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.mapper.UserMapper;
import com.ticket.reservation.model.request.AddUserReq;
import com.ticket.reservation.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ticket.reservation.model.entity.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public void register(AddUserReq addUserReq){
        // 判断用户名是否存在
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", addUserReq.getUsername());
        if (userMapper.selectOne(query) != null) {
             throw new TicketSystemException(TicketSystemExceptionEnum.USERNAME_EXIST);
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(addUserReq, user);
        if (user.getUsername().length() < 1){
            throw new TicketSystemException(TicketSystemExceptionEnum.NEED_USER_NAME);
        }
        if (user.getPassword().length() < 1){
            throw new TicketSystemException(TicketSystemExceptionEnum.NEED_USER_NAME);
        }
        userMapper.insert(user);
    }

    @Override
    public User login(String username, String password) {
        // 判断用户名是否存在
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        query.eq("password", password);
        User user = userMapper.selectOne(query);
        if (user == null){
            throw new TicketSystemException(TicketSystemExceptionEnum.LOGIN_ERROR);
        }
//        user.setPassword(null);
        return user;
    }

    @Override
    public void update(User user) {
        int i = userMapper.updateById(user);
        if (i == 0){
            throw new TicketSystemException(TicketSystemExceptionEnum.UPDATE_FAILED);
        }
    }
}
