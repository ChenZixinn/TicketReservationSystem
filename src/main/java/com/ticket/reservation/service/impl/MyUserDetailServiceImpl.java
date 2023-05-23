package com.ticket.reservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.mapper.UserMapper;
import com.ticket.reservation.model.entity.SecurityUser;
import com.ticket.reservation.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailService")
public class MyUserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        System.out.println("username:" + s);
        queryWrapper.eq("username", s);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
        if (user == null) {
            throw new TicketSystemException(TicketSystemExceptionEnum.USER_NOT_FOUND);
        }
        //获取用户权限，并把其添加到GrantedAuthority中
//        List<GrantedAuthority> auths =new ArrayList<>();
//        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority(user.getRole());
        List<String> auths =new ArrayList<>();
        auths.add(user.getRole());
        CustomerFilter.currentUser = user;

        SecurityUser securityUser = new SecurityUser();
        securityUser.setPermissionValueList(auths);
        securityUser.setCurrentUserInfo(user);

//        return new org.springframework.security.core.userdetails.User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()), auths);
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), auths);
        return securityUser;
    }
}
