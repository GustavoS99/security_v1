package com.emazon.user_v1.infrastructure.config;

import com.emazon.user_v1.domain.api.IUserServicePort;
import com.emazon.user_v1.domain.auth.IAuthenticationPort;
import com.emazon.user_v1.domain.spi.IRolePersistencePort;
import com.emazon.user_v1.domain.spi.IUserPersistencePort;
import com.emazon.user_v1.domain.usecase.UserUseCase;
import com.emazon.user_v1.infrastructure.out.jpa.adapter.RoleJpaAdapter;
import com.emazon.user_v1.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.emazon.user_v1.infrastructure.out.jpa.mapper.RoleEntityMapper;
import com.emazon.user_v1.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IRoleRepository;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IUserRepository;
import com.emazon.user_v1.infrastructure.out.jwt.adapter.AuthenticationAdapter;
import com.emazon.user_v1.infrastructure.out.jwt.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IAuthenticationPort authenticationPort() {
        return new AuthenticationAdapter(passwordEncoder, jwtUtils, authenticationManager);
    }

    @Bean
    public IRolePersistencePort rolePersistencePort() {
        return new RoleJpaAdapter(roleRepository, roleEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(userPersistencePort(), rolePersistencePort(), authenticationPort());
    }
}
