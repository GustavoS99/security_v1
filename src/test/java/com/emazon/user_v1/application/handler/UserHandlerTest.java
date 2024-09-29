package com.emazon.user_v1.application.handler;

import com.emazon.user_v1.application.dto.LoginRequest;
import com.emazon.user_v1.application.dto.LoginResponse;
import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.application.mapper.*;
import com.emazon.user_v1.domain.api.IUserServicePort;
import com.emazon.user_v1.domain.model.Login;
import com.emazon.user_v1.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserRequestMapperImpl userRequestMapper;

    @Mock
    private LoginRequestMapperImpl loginRequestMapper;

    @Mock
    private LoginResponseMapperImpl loginResponseMapper;

    @InjectMocks
    private UserHandler userHandler;

    private UserRequest workerUserRequest;
    private LoginRequest loginRequest;
    private UserRequest customerUserRequest;

    @BeforeEach
    void setUp() {

        workerUserRequest = UserRequest.builder()
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .birthDate("1990-01-01")
                .identification(876436432L)
                .build();

        loginRequest = new LoginRequest(workerUserRequest.getEmail(), workerUserRequest.getPassword());
    }

    @Test
    void when_saveCustomer_expect_callToServicePort() {

        customerUserRequest = UserRequest.builder()
                .firstName("Messi")
                .lastName("Perez")
                .email("messi@email.com")
                .password("kjfd89732jfre8732e")
                .birthDate("1995-01-23")
                .identification(87453873276L)
                .build();

        doCallRealMethod().when(userRequestMapper).toUser(any(UserRequest.class));

        doNothing().when(userServicePort).saveWarehouseWorker(any(User.class));

        userHandler.saveWarehouseWorker(customerUserRequest);

        verify(userServicePort, times(1)).saveWarehouseWorker(any(User.class));
    }

    @Test
    void when_saveWarehouseWorker_expect_callToServicePort() {

        doCallRealMethod().when(userRequestMapper).toUser(any(UserRequest.class));

        doNothing().when(userServicePort).saveWarehouseWorker(any(User.class));

        userHandler.saveWarehouseWorker(workerUserRequest);

        verify(userServicePort, times(1)).saveWarehouseWorker(any(User.class));
    }

    @Test
    void when_authenticate_expect_callToServicePort() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        Login expectedLogin = new Login();
        expectedLogin.setToken(token);

        doCallRealMethod().when(loginRequestMapper).toLogin(any(LoginRequest.class));
        when(userServicePort.authenticate(any(Login.class))).thenReturn(expectedLogin);
        doCallRealMethod().when(loginResponseMapper).toLoginResponse(any(Login.class));

        LoginResponse result = userHandler.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals(token, result.getToken());
    }
}