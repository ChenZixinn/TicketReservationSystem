package com.ticket.reservation.handler;

import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.model.entity.CustomAuthenticationToken;
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

        if (authentication instanceof CustomAuthenticationToken) {
            // 根据自定义逻辑进行认证
            CustomAuthenticationToken customToken = (CustomAuthenticationToken) authentication;
            String token = customToken.getToken();

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String account = authentication.getName();     //获取用户名
            String password = (String) authentication.getCredentials();  //获取密码
            SecurityUser userDetails = (SecurityUser) userDetailService.loadUserByUsername(account);
            System.out.println("password" + password);
            System.out.println("userDetails" + userDetails);
            boolean checkPassword = bCryptPasswordEncoder.matches(password, userDetails.getCurrentUserInfo().getPassword());
            if (!checkPassword) {
                throw new BadCredentialsException("密码不正确，请重新登录!");
            }

            // 如果认证成功，创建一个新的认证令牌并返回
            Authentication authenticatedToken = new CustomAuthenticationToken(userDetails, password, userDetails.getAuthorities(), token);
//            authenticatedToken.setAuthenticated(checkPassword);
            return authenticatedToken;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
