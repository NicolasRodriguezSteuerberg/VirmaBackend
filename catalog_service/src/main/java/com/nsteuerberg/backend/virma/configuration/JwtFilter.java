package com.nsteuerberg.backend.virma.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.nsteuerberg.backend.virma.service.implementation.AuthServiceImpl;
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
public class JwtFilter extends OncePerRequestFilter{

    private final AuthServiceImpl authService;

    public JwtFilter(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null) {
            DecodedJWT decodedJWT = authService.validateToken(token);

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(
                    authService.extractUsername(decodedJWT),
                    null,
                    authService.authorities(decodedJWT)
            ));
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }
}
