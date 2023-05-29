package com.ticket.reservation.handler;

import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.model.entity.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserDetailsService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String account = authentication.getName();     //获取用户名
        String password = (String) authentication.getCredentials();  //获取密码
        SecurityUser userDetails = (SecurityUser) userDetailService.loadUserByUsername(account);
        boolean checkPassword = bCryptPasswordEncoder.matches(password, userDetails.getCurrentUserInfo().getPassword());
        if (!checkPassword) {
            throw new BadCredentialsException("密码不正确，请重新登录!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
