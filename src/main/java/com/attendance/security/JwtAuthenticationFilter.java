package com.attendance.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService
            customUserDetailsService;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            CustomUserDetailsService customUserDetailsService) {

        this.jwtUtil = jwtUtil;
        this.customUserDetailsService =
                customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
    	
    	String servletPath =
    	        request.getServletPath();

    	if (servletPath.startsWith("/swagger-ui")
    	        || servletPath.startsWith("/v3/api-docs")) {

    	    filterChain.doFilter(request, response);
    	    return;
    	}

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response);

            return;
        }

        String token =
                authHeader.substring(7);

        String email =
                jwtUtil.extractEmail(token);
        System.out.println("EMAIL = " + email);

        if (email != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            UserDetails userDetails =
                    customUserDetailsService
                            .loadUserByUsername(email);

            if (jwtUtil.validateToken(token)) {

                UsernamePasswordAuthenticationToken
                        authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authenticationToken
                        .setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request));
                
                System.out.println("AUTHENTICATION SUCCESS");

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authenticationToken);
            }
        }

        filterChain.doFilter(
                request,
                response);
    }
}