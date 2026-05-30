package org.platform.repair.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.platform.repair.entity.User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET =
            "my-super-secret-key-my-super-secret-key";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    private static final long EXPIRATION =
            86400000L;

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("shopId", user.getShop().getId())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis() + EXPIRATION
                        )
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long extractShopId(String token) {

        Object value =
                getClaims(token).get("shopId");

        if (value instanceof Integer i) {
            return i.longValue();
        }

        if (value instanceof Long l) {
            return l;
        }

        return Long.valueOf(value.toString());
    }

    public boolean isValid(String token) {

        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}