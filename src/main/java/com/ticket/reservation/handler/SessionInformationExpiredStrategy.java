package com.ticket.reservation.handler;

import com.google.gson.Gson;
import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//异地登录、账号下线
@Component
public class SessionInformationExpiredStrategy implements org.springframework.security.web.session.SessionInformationExpiredStrategy{
    @Autowired
    Gson gson;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        ApiRestResponse<String> result = ApiRestResponse.error(TicketSystemExceptionEnum.LOGIN_ERROR_PLACE);
        HttpServletResponse response=event.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}
