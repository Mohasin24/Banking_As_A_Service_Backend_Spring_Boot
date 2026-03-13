package com.authentication_service.JWT;

import com.authentication_service.entity.Auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtService(){}

    public String generateToken(Auth user){

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId",user.getUserId());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*72))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsernameFromToken(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUsernameFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private SecretKey getSecretKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenExpired(String token){
        return extractTokenExpiration(token).before(new Date());
    }

    public Date extractTokenExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
}
