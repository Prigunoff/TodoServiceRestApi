package com.prigunoff.todolist.security.jwt;

import com.prigunoff.todolist.exceptions.JwtAuthException;
import com.prigunoff.todolist.model.Role;
import com.prigunoff.todolist.security.service.JwtUserDetailServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secret;
    private final JwtUserDetailServiceImpl jwtUserDetailService;

    @Autowired
    public JwtProvider(JwtUserDetailServiceImpl jwtUserDetailService) {
        this.jwtUserDetailService = jwtUserDetailService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String createToken(String username, Role role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", getRoleName(role));
        Date now = new Date();
        Date date = Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthException("Jwt token is invalid");
        }
    }

    private String getRoleName(Role accountRole) {
        return accountRole.getName();
    }
}
