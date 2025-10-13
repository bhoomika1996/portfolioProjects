package com.bhoomika.ExpenseTracker.security;

// JwtFilter.java_v3
// Filter for validating JWT and setting Spring Security context
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends GenericFilterBean {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                Long userId = jwtUtil.extractUserId(jwt);
                String role = jwtUtil.extractRole(jwt);
                System.out.println("🔍 Role from token: " + role);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                System.out.println("🔐 Granted Authority: " + authority); // Debug

                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, List.of(authority));
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Optional

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("✅ Authentication set in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());

            } catch (JwtException e) {
                // Invalid token, no authentication set
            }
        }
        chain.doFilter(req, res);
    }
}

