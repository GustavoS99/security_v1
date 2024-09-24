package com.emazon.user_v1.infrastructure.out.jwt.adapter;

import com.emazon.user_v1.domain.auth.IAuthenticationPort;
import com.emazon.user_v1.domain.model.Login;
import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.infrastructure.out.jwt.util.JwtUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AuthenticationAdapter implements IAuthenticationPort {

    private static final String COMMA_DELIMITER = ",";
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    @Override
    public String getToken(User user) {
        String username = user.getId().toString();
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_".concat(user.getRole().getName().name())));

        return jwtUtils.createToken(new UsernamePasswordAuthenticationToken(
                username, user.getPassword(), simpleGrantedAuthorities
        ));
    }

    @Override
    public void authenticate(Login login) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(login.getUsername(), login.getPassword());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
    }
}
