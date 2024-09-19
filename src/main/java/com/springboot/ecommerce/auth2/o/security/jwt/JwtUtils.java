package com.springboot.ecommerce.auth2.o.security.jwt;

import com.springboot.ecommerce.auth2.o.security.user.ShopUserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * @author prabhakar, @Date 11-09-2024
 */
@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication){
        ShopUserDetail userPrincipal = (ShopUserDetail) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(userPrincipal.getEmail())
                .claim("id",userPrincipal.getId())
                .claim("roles",roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()+expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromToken(String token){
       return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException |UnsupportedJwtException |MalformedJwtException |SecurityException |IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }



}
