package com.example.androidprojectserver.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String idToken = extractToken(request);

            if (idToken != null) {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                Authentication authentication = new UsernamePasswordAuthenticationToken(decodedToken, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 예외 처리 로직 추가
            throw new ServletException("Firebase Authentication failed", e);
        }
    }

    private String extractToken(HttpServletRequest request) {
        // 클라이언트에서 토큰 추출 로직을 구현
        // 예: Authorization 헤더에서 "Bearer " 다음의 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
