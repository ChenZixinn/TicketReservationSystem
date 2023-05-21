package com.ticket.reservation.common;

import com.alibaba.fastjson.JSON;
import com.ticket.reservation.model.vo.OrderTicketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    RedisTemplate redisTemplate;

    public void set(String key, Object data){
        redisTemplate.opsForValue().set(key, data);
    }
    public void set(String key, Object data, long l, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, data, l, timeUnit);
    }

    public <T> T get(String key, Class<T> clazz){
        T o =  JSON.parseObject((String) redisTemplate.opsForValue().get(key), clazz);
        return o;
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
