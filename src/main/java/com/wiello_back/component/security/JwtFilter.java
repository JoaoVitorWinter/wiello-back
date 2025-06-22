package com.wiello_back.component.security;

import com.wiello_back.entity.WielloUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new Exception();
            }
            String tokenJwt = authorization.substring(7);
            Authentication authentication = jwtHelper.validateToken(tokenJwt);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            String newToken = jwtHelper.createToken((WielloUser) authentication.getPrincipal());
            response.addHeader("revalidatedtoken", newToken);
        } catch (Exception exception) {
            if (!isPublicEndpoint(uri, method)) {
                response.setStatus(401);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String uri, String method) {
        return uri.equals("/user/login") && method.equals("POST") ||
            uri.equals("/user") && method.equals("POST");
    }
}
