package com.ticket.reservation.handler;

import com.google.gson.Gson;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.common.RedisUtils;
import com.ticket.reservation.common.TokenManager;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.model.entity.SecurityUser;
import com.ticket.reservation.model.entity.User;
import com.ticket.reservation.model.vo.UserTokenVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler{
    @Autowired
    Gson gson;

    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private RedisUtils redisUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 登陆成功将token存到redis
        SecurityUser secUser = (SecurityUser)authentication.getPrincipal();
        User user = secUser.getCurrentUserInfo();

        String token = tokenManager.createToken(user.getUsername());
//        redisUtils.set(user.getUsername(), secUser.getPermissionValueList());

        user.setPassword("");
        UserTokenVO userTokenVO = new UserTokenVO();
        BeanUtils.copyProperties(user, userTokenVO);
        userTokenVO.setToken(token);
        ApiRestResponse<Object> msg = ApiRestResponse.success(userTokenVO);
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //返回给前台
        response.getWriter().write(gson.toJson(msg));
    }
}
