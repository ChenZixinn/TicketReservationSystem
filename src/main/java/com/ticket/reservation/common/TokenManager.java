package com.ticket.reservation.common;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {
    /**
     * token有效时长
     */
    private final long tokenExpiration = 24*60*60*1000;
    /**
     * 编码密钥
     */
    private final String tokenSignKey = "abcdef";

    /**
     * 根据用户名生成token
     * @param username 用户名
     */
    public String createToken(String username){
        return Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
    }
    /**
     * 根据token字符串得到用户信息
     * @param token token字符串
     */
    public String getUserInfoFromToken(String token){
        return Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
    }

}
