package com.emazon.user_v1.infrastructure.out.jwt.adapter;

import com.emazon.user_v1.domain.model.Login;
import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.infrastructure.out.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationAdapter authenticationAdapter;

    private String rawPassword;
    private String encodedPassword;
    private User user;

    @BeforeEach
    void setUp() {
        rawPassword = "password";
        encodedPassword = "encodedPassword";


        user = User.builder()
                .id(2L)
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .phoneNumber("+573003216598")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(876436432L)
                .role(new Role(2L, RoleEnum.WAREHOUSE_WORKER, "Wharehouse worker"))
                .build();
    }

    @Test
    void whe_encode_expects_successfullyEncodedPassword() {

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        String result = authenticationAdapter.encode(rawPassword);

        assertNotNull(result);
        assertEquals(encodedPassword, result);
    }

    @Test
    void when_matchesValidPassword_expect_successfullyMatchedPassword() {

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Boolean result = authenticationAdapter.matches(rawPassword, encodedPassword);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    void when_matchesInvalidPassword_expect_successfullyMatchedPassword() {

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Boolean result = authenticationAdapter.matches(rawPassword, encodedPassword);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    void when_getToken_expect_successfullyGeneratedToken() {

        String token = "token";

        when(jwtUtils.createToken(any(Authentication.class))).thenReturn(token);

        String result = authenticationAdapter.getToken(user);

        assertNotNull(result);
        assertEquals(token, result);
    }

    @Test
    void when_authenticate_expect_successCallToAuthenticationManager() {
        Login login = Login.builder()
                .username("test@mail.com")
                .password("12234")
                .build();

        authenticationAdapter.authenticate(login);

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }



    @Test
    void expect_AuthenticationException_when_authenticationFailed() {
        Login login = Login.builder()
                .username("test@mail.com")
                .password("12234")
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new AuthenticationCredentialsNotFoundException(""));

        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> authenticationAdapter.authenticate(login));
    }
}