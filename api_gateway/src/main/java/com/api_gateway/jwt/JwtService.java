package com.api_gateway.jwt;

import com.api_gateway.exception.JwtTokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService
{
    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey generateKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date extractTokenExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token ){
        return extractTokenExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token){
        if(isTokenExpired(token)){
            throw new JwtTokenExpiredException("Session Expired! Please login again.");
        }
        return true;
    }
}
