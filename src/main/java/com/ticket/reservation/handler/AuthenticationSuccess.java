package com.ticket.reservation.handler;

import com.google.gson.Gson;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.model.entity.User;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User currentUser = CustomerFilter.currentUser;
        currentUser.setPassword("");
        ApiRestResponse<Object> msg = ApiRestResponse.success(currentUser);
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //返回给前台
        response.getWriter().write(gson.toJson(msg));
    }
}
