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
import org.springframework.security.core.context.SecurityContextHolder;
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
