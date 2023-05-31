package com.ticket.reservation.controller;

import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.common.TokenManager;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.model.entity.CustomAuthenticationToken;
import com.ticket.reservation.model.entity.SecurityUser;
import com.ticket.reservation.model.entity.User;
import com.ticket.reservation.model.request.AddUserReq;
import com.ticket.reservation.model.request.LoginUser;
import com.ticket.reservation.model.request.UpdateUserReq;
import com.ticket.reservation.service.UserService;
import com.ticket.reservation.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.beans.FeatureDescriptor;
import java.util.Arrays;

@RestController
@Api("用户管理")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/api/login")
    public ApiRestResponse login(String username, String password) {
        System.out.println("进入登陆");
        String token = tokenManager.createToken(username);
        try {
            // 进行身份验证
            System.out.println("1111:");
            System.out.println(password);
            Authentication authentication = authenticationManager.authenticate(new CustomAuthenticationToken(username, password, token));

            // 将认证信息存储到SecurityContext中
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 返回登录成功的响应
            return ApiRestResponse.success(((SecurityUser)authentication.getPrincipal()).getCurrentUserInfo());
        } catch (AuthenticationException ex) {
            // 处理登录失败的情况
            return ApiRestResponse.error(TicketSystemExceptionEnum.LOGIN_ERROR);
        }
    }

    @PostMapping("/api/logout")
    public ApiRestResponse logout() {
        // 执行注销操作，清除认证信息
        SecurityContextHolder.clearContext();

        // 返回注销成功的响应
        return ApiRestResponse.success("注销成功");
    }

    @ApiOperation("注册接口")
    @PostMapping("/api/register")
    public ApiRestResponse register(@Valid @RequestBody AddUserReq addUserReq){
        // 调用service层接口
        userService.register(addUserReq);
        return ApiRestResponse.success();
    }

    @ApiOperation("获取状态")
    @GetMapping("/api/user/userinfo")
    public ApiRestResponse userinfo(){
        return ApiRestResponse.success(userService.getUserInfo());
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/api/user/update_user")
    public ApiRestResponse update_user(@RequestBody @Valid UpdateUserReq updateUserReq){
        userService.update(updateUserReq);
        return ApiRestResponse.success();
    }


}
