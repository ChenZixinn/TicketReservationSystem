package com.ticket.reservation.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ticket.reservation.common.Constant;
import com.ticket.reservation.common.RedisUtils;
import com.ticket.reservation.exception.TicketSystemException;
import com.ticket.reservation.exception.TicketSystemExceptionEnum;
import com.ticket.reservation.filter.CustomerFilter;
import com.ticket.reservation.mapper.UserMapper;
import com.ticket.reservation.model.entity.SecurityUser;
import com.ticket.reservation.model.request.AddUserReq;
import com.ticket.reservation.model.request.UpdateUserReq;
import com.ticket.reservation.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ticket.reservation.model.entity.User;
import springfox.documentation.spring.web.json.Json;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    /**
     * redis工具类
     */
    @Autowired
    RedisUtils redisUtils;

    /**
     * user的mapper，数据库操作
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户
     * @param addUserReq 用户注册信息
     */
    public void register(AddUserReq addUserReq) {
        // 判断用户名是否存在
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", addUserReq.getUsername());
        // 已经存在用户返回错误
        if (userMapper.selectOne(query) != null) {
            throw new TicketSystemException(TicketSystemExceptionEnum.USERNAME_EXIST);
        }
        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(addUserReq, user);
        // 名称长度小于1返回错误
        if (user.getUsername().length() < 1) {
            throw new TicketSystemException(TicketSystemExceptionEnum.NEED_USER_NAME);
        }
        // 密码长度小于1返回错误
        if (user.getPassword().length() < 1) {
            throw new TicketSystemException(TicketSystemExceptionEnum.NEED_PASSWORD);
        }
        // 密码编码
        String password = new BCryptPasswordEncoder().encode(user.getPassword());
        // 编码后重新设置密码
        user.setPassword(password);
        // 插入到数据库
        int id = userMapper.insert(user);
        // 存入redis，过期时间10小时
        redisUtils.set(Constant.USER_INFO_KEY + id, JSON.toJSONString(user), 10, TimeUnit.HOURS);
    }

    /**
     * 获取用户信息
     * @return 返回当前登陆的用户信息
     */
    @Override
    public User getUserInfo() {
        User user = null;

        // 从Spring Security里取到用户id，到数据库里查
        SecurityUser secUser = (SecurityUser)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        user = secUser.getCurrentUserInfo();
        int id = user.getId();

        // 从redis里取
        user = redisUtils.get(Constant.USER_INFO_KEY + id, User.class);
        // 不返回密码
        user.setPassword("");
        if (user != null){
            return user;
        }

        // 如果redis查不到，到mySQL里查
        QueryWrapper<User> query = new QueryWrapper<>();
        // 根据id查
        query.eq("id", id);
        user = userMapper.selectOne(query);
        // 去掉密码
        user.setPassword("");

        // 查到之后存到redis，key="user_info_key:" + id
        redisUtils.set(Constant.USER_INFO_KEY + user.getId(), JSON.toJSONString(user), 12, TimeUnit.HOURS);

        // 返回user
        return user;
    }

    /**
     * 更新用户信息
     * @param updateUserReq 要更新的信息，不修改的可以为空
     */
    @Override
    public void update(UpdateUserReq updateUserReq) {
        User user = null;

        // 从Spring Security里拿到用户信息
        SecurityUser secUser = (SecurityUser)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        user = secUser.getCurrentUserInfo();
        // 拿到id
        int id = user.getId();
        // redis查user
        user = redisUtils.get(Constant.USER_INFO_KEY + id, User.class);
        if (user == null){
            // redis查不到就到MySQL查
            QueryWrapper<User> query = new QueryWrapper<>();
            query.eq("id", id);
            user = userMapper.selectOne(query);
            if (user == null){
                // MySQL查不到返回错误，找不到用户
                throw new TicketSystemException(TicketSystemExceptionEnum.USER_NOT_FOUND);
            }
        }
        // 复制要更新的属性到原User对象上
        BeanUtils.copyProperties(updateUserReq, user, getNullPropertyNames(updateUserReq));
        // 在数据库里更新信息
        String passwd = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(passwd);
        int i = userMapper.updateById(user);
        // 没更新成功返回报错信息
        if (i == 0) {
            throw new TicketSystemException(TicketSystemExceptionEnum.UPDATE_FAILED);
        }
        // 删掉redis的数据，保证数据一致
        redisUtils.delete(Constant.USER_INFO_KEY + user.getId());

    }

    /**
     * 获取不为空的参数
     * @param source 参数
     * @return 字符串列表
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }
}
