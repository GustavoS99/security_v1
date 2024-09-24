package com.emazon.user_v1.infrastructure.out.jwt.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.emazon.user_v1.infrastructure.exception.response.ExceptionResponse;
import com.emazon.user_v1.infrastructure.out.jwt.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import static com.emazon.user_v1.util.GlobalConstants.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String jwt;
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || Boolean.FALSE.equals(header.startsWith(BEARER))) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = header.substring(BEARER.length()).trim();
        DecodedJWT decodedJWT;
        String id;
        String stringAuthorities;
        try {
            decodedJWT = jwtUtils.validateToken(jwt);
            id = decodedJWT.getSubject();
            stringAuthorities = decodedJWT.getClaim("authorities").asString();
        } catch (Exception e) {
            senError(response, new Exception(ExceptionResponse.INVALID_TOKEN.getMessage()));
            return;
        }

        if(id == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(stringAuthorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(id, null, authorities);
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

    private void senError(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        writer.println(mapper.writeValueAsString(Map.of("error", exception.getMessage(), "code", "-1")));
        writer.flush();
    }
}
