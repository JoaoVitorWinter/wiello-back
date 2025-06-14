package com.wiello_back.component.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.entity.WielloUserRole;
import com.wiello_back.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtHelper {
    private final AuthenticationService authenticationService;
    @Value("${jwt.security.secret}")
    private String secret;

    public String createToken(WielloUser wielloUser) {
        Instant instantIssuedAt = Instant.now();
        Instant instantExpireAt = instantIssuedAt.plus(1, ChronoUnit.HOURS);
        String[] authorities = wielloUser.getAuthorities().stream()
                .map(WielloUserRole::getAuthority).toArray(String[]::new);
        return JWT.create()
                .withSubject(wielloUser.getUsername())
                .withIssuedAt(instantIssuedAt)
                .withExpiresAt(instantExpireAt)
                .withArrayClaim("authorities", authorities)
                .sign(Algorithm.HMAC256(secret));
    }

    public Authentication validateToken(String token) {
        String username = JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getSubject();
        WielloUser wielloUser = (WielloUser) authenticationService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(wielloUser, wielloUser.getPassword(), wielloUser.getAuthorities());
    }
}
