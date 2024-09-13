package com.emazon.user_v1.infrastructure.input.rest;

import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.application.handler.IUserHandler;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntity;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntityEnum;
import com.emazon.user_v1.infrastructure.out.jpa.entity.UserEntity;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static com.emazon.user_v1.infrastructure.input.rest.utils.PathDefinition.SIGNUP_WAREHOUSE_WORKER;
import static com.emazon.user_v1.infrastructure.input.rest.utils.PathDefinition.USER;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @SpyBean
    private IUserHandler userHandler;

    @MockBean
    private IUserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private String userJson;
    private UserEntity userEntity;
    private Optional<UserEntity> optionalUserEntity;

    @BeforeEach
    void setUp() {
        userJson = """
                {
                    "firstName": "Ronaldo",
                    "lastName": "Ramirez",
                    "identification": 876436432,
                    "phoneNumber": "+573003216598",
                    "birthDate": "1990-01-01",
                    "email": "ronaldo@email.com",
                    "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                }
                """;
        RoleEntity roleEntity = new RoleEntity(2L, RoleEntityEnum.WAREHOUSE_WORKER, "Wharehouse worker");

        userEntity = UserEntity.builder()
                .id(2L)
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .phoneNumber("+573003216598")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(876436432L)
                .role(roleEntity)
                .build();

        optionalUserEntity = Optional.of(userEntity);
    }

    @Test
    void when_saveWarehouseWorker_expect_statusCreated() throws Exception {

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());
    }

    private static Stream<Arguments> providedExpect_badRequest_when_requestValidationFails() {
        return Stream.of(
                Arguments.of("""
                        {
                            "firstName": "",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": " ",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": null,
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": " ",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": null,
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": null,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": null,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+5730598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": " ",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": null,
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "30598",
                            "birthDate": "1990-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "2020-01-01",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": " ",
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": null,
                            "email": "ronaldo@email.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": " ",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": null,
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "user@com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "user@domain..com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "@example.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "user@.com",
                            "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "user@email.com",
                            "password": ""
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "user@email.com",
                            "password": " "
                        }
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": 876436432,
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "user@email.com",
                            "password": null
                        }
                        """)
        );
    }

    @ParameterizedTest
    @MethodSource("providedExpect_badRequest_when_requestValidationFails")
    void expect_badRequest_when_requestValidationFails(String userJsonBadRequest) throws Exception {
        doCallRealMethod().when(userHandler).saveWarehouseWorker(any(UserRequest.class));

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJsonBadRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void expect_conflict_when_userEmailExists() throws Exception {

        when(userRepository.findByEmail(anyString())).thenReturn(optionalUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isConflict());
    }

    @Test
    void expect_conflict_when_userIdentificationExists() throws Exception {

        when(userRepository.findByIdentification(anyLong())).thenReturn(optionalUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict());
    }

    @Test
    void expect_conflict_when_userPhoneNumberExists() throws Exception {

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(optionalUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict());
    }
}