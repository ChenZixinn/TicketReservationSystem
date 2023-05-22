package com.ticket.reservation.handler;

import com.google.gson.Gson;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.filter.CustomerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import sun.security.jgss.GSSCaller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationLogout implements LogoutSuccessHandler{
    @Autowired
    Gson gson;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ApiRestResponse<Object> msg = ApiRestResponse.success("注销成功！");
        CustomerFilter.currentUser = null;
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //返回给前台
        response.getWriter().write(gson.toJson(msg));
    }
}
