package com.ticket.reservation.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    String ip;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        // 设置Redisson服务器地址
        config.useSingleServer().
                setAddress("redis://"+ip+":6379")
                .setPassword(redisPassword);
        return Redisson.create(config);
    }
}
