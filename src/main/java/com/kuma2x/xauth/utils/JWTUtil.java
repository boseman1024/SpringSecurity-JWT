package com.kuma2x.xauth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameByToken(String token){
        String username;
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }

    public Boolean isTokenExpired(String Token){
        Claims claims;
        try{
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(Token).getBody();
        }catch (Exception e){
            claims = null;
        }
        return claims.getExpiration().before(new Date());
    }

    public String initToken(UserDetails userDetails){
        //生成数据声明claims
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub",userDetails.getUsername());
        claims.put("created",new Date());

        String Token = Jwts.builder().setClaims(claims)
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis()+expiration*1000))
                //加密算法+盐
                .signWith(SignatureAlgorithm.HS512,secret).compact();
        return Token;
    }

    public Boolean valiadateToken(String Token,UserDetails userDetails){
        String username = getUsernameByToken(Token);
        Boolean result = (!isTokenExpired(Token)&& username.equals(userDetails.getUsername()));
        return result;
    }
}
