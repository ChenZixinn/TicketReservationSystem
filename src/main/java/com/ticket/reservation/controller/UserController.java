package com.ticket.reservation.controller;


import com.ticket.reservation.common.ApiRestResponse;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.filter.CustomerFilter;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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

    @ApiOperation("注册接口")
    @PostMapping("/api/register")
    public ApiRestResponse register(@Valid @RequestBody AddUserReq addUserReq){
        System.out.println(addUserReq);
        userService.register(addUserReq);
        return ApiRestResponse.success();
    }

//    @ApiOperation("登陆接口")
////    @PostMapping("/api/login")
////    public ApiRestResponse login(@Valid @RequestBody LoginUser loginUser, @ApiIgnore HttpSession session){
////        System.out.println(loginUser);
////        User user = userService.login(loginUser.getUsername(), loginUser.getPassword());
////        // 保存到session
////        session.setAttribute(Constant.TICKET_SYSTEM_USER, user);
//////        CustomerFilter.currentUser = user;
////        user.setPassword(null);
////        return ApiRestResponse.success(user);
////    }

    @ApiOperation("获取状态")
    @GetMapping("/api/user/userinfo")
    public ApiRestResponse userinfo(){
        return ApiRestResponse.success(CustomerFilter.currentUser);
    }


//    @ApiOperation("退出登陆")
//    @GetMapping("/api/user/logout")
//    public ApiRestResponse logout(@ApiIgnore HttpSession session){
//        // 退出登陆
//        session.removeAttribute(Constant.TICKET_SYSTEM_USER);
//        return ApiRestResponse.success();
//    }

    @ApiOperation("更新用户信息")
    @PostMapping("/api/user/update_user")
    public ApiRestResponse update_user(@RequestBody @Valid UpdateUserReq updateUserReq){
        User user = CustomerFilter.currentUser;
        BeanUtils.copyProperties(updateUserReq, user, getNullPropertyNames(updateUserReq));
        userService.update(user);
        return ApiRestResponse.success();
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }
}
