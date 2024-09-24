package com.emazon.user_v1.infrastructure.out.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import static com.emazon.user_v1.util.GlobalConstants.EMAIL_USER_INDEX;
import static com.emazon.user_v1.util.GlobalConstants.ID_USER_INDEX;

@Component
public class JwtUtils {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    @Value("${security.jwt.expiration-time}")
    private int expirationTime;

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String id = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities().iterator().next().toString();

        return JWT.create()
                .withIssuer(userGenerator)
                .withSubject(id)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(userGenerator)
                .build();

        return verifier.verify(token);
    }
}
