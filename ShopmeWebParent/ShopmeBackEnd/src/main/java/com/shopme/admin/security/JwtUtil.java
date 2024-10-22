package com.shopme.admin.security;

import com.shopme.admin.exceptions.BizException;
import com.shopme.admin.pojo.response.BaseResponseEnum;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;

@Component
public class JwtUtil {

    @Value("yz2VmR8YtcbJRQ4PXqhREC6BpK4tE3aPobasTiLoP")
    private String secret;

    @Value("7d")
    private Duration expiration;

    @Autowired
    private UserRepository userRepository;

    public String generateLoginJwtToken(User userLogin) {
        try {
            return Jwts.builder()
                    .setSubject(String.valueOf(userLogin.getId()))
                    .claim("email", userLogin.getEmail())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(Date.valueOf(LocalDate.now().plusDays(expiration.toDays())))
                    .signWith(getSecretKey())
                    .compact();

        } catch (Exception ex) {
//            log.error("generateLoginJwtToken: {}", ex.getMessage());
            throw new BizException(BaseResponseEnum.SERVER_ERROR, "Can't generate token");
        }
    }

    public User validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            Claims body = claims.getBody();
            String userId = body.getSubject();
            String email = body.get("email").toString();

            User userLogin = new User();
            userLogin.setId(Integer.valueOf(userId));
            userLogin.setEmail(email);
            return userLogin;
        } catch (Exception ex) {
//            log.error("validateToken: error - {}", ex.getMessage());
            return null;
        }
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
