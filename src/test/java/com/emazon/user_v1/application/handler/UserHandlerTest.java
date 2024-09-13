package com.emazon.user_v1.application.handler;

import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.application.mapper.UserRequestMapper;
import com.emazon.user_v1.domain.api.IUserServicePort;
import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserRequestMapper userRequestMapper;

    @InjectMocks
    private UserHandler userHandler;

    private User workerUser;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        Role workerRole = new Role(2L, RoleEnum.WAREHOUSE_WORKER, "Auxiliar de bodega");

        workerUser = User.builder()
                .id(2L)
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(876436432L)
                .role(workerRole)
                .build();

        userRequest = UserRequest.builder()
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .birthDate(Date.from(Instant.ofEpochMilli(431964635000L)).toString())
                .identification(876436432L)
                .build();
    }

    @Test
    void when_saveWarehouseWorker_expect_callToServicePort() {
        when(userRequestMapper.toUser(any(UserRequest.class))).thenReturn(workerUser);

        doNothing().when(userServicePort).saveWarehouseWorker(any(User.class));

        userHandler.saveWarehouseWorker(userRequest);

        verify(userServicePort, times(1)).saveWarehouseWorker(workerUser);
    }
}