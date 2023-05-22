package com.ticket.reservation.handler;


import com.google.gson.Gson;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import org.apache.catalina.session.TooManyActiveSessionsException;
import org.opentest4j.TestSkippedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEnryPoint implements AuthenticationEntryPoint {
    @Autowired
    Gson gson;
    //未登录时返回给前端数据
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ApiRestResponse<Object> msg = ApiRestResponse.error(TicketSystemExceptionEnum.NEED_LOGIN);
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //返回给前台
        response.getWriter().write(gson.toJson(msg));
    }
}
