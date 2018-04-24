package com.meetup.meetup.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String authorization = request.getHeader("Authorization");

        if (authorization != null) {

            String token = authorization.replaceAll("Bearer ", "");
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !token.equals(authentication.getCredentials().toString())) {
                JwtAuthToken jwtAuthToken = new JwtAuthToken(token);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthToken);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
