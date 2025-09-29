package com.nsteuerberg.virma.auth_service.configuration.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.nsteuerberg.virma.auth_service.util.token.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtValidator validator;

    public JwtFilter(JwtValidator validator) {
        this.validator = validator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            DecodedJWT decodedJWT = validator.validateToken(token);

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(
                    validator.extractUsername(decodedJWT),
                    null,
                    validator.extractAuthorities(decodedJWT)
            ));
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }
}
