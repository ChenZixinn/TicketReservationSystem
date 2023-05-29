package com.ticket.reservation.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ticket.reservation.model.entity.Ticket;
import com.ticket.reservation.model.vo.OrderTicketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public <T> Page<T> getPage(String key, Class<T> clazz){
        // 使用 TypeReference 指定泛型类型
        Type type = new ParameterizedType() {
            public Type[] getActualTypeArguments() {
                return new Type[]{clazz};
            }
            public Type getRawType() {
                return Page.class;
            }
            public Type getOwnerType() {
                return null;
            }
        };


        // 将 JSON 字符串转换为 Page<Ticket> 对象
        Page<T> ticketPage = JSON.parseObject((String) redisTemplate.opsForValue().get(key), type);
        return ticketPage;
    }

    public <T> T get(String key, Class<T> clazz){
        T o =  JSON.parseObject((String) redisTemplate.opsForValue().get(key), clazz);
        return o;
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
