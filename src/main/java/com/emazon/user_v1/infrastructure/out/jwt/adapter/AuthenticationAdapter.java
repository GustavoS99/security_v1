package com.emazon.user_v1.infrastructure.out.jwt.adapter;

import com.emazon.user_v1.domain.auth.IAuthenticationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AuthenticationAdapter implements IAuthenticationPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
