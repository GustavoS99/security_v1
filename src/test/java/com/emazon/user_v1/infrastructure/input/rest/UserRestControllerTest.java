package com.emazon.user_v1.infrastructure.input.rest;

import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntity;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntityEnum;
import com.emazon.user_v1.infrastructure.out.jpa.entity.UserEntity;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IUserRepository;
import com.emazon.user_v1.infrastructure.out.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.emazon.user_v1.infrastructure.input.rest.util.PathDefinition.*;
import static com.emazon.user_v1.util.GlobalConstants.ADMIN;
import static com.emazon.user_v1.util.GlobalConstants.BEARER;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserRestControllerTest {

    public static final String ADMIN_USERNAME = "1";

    @MockBean
    private IUserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String userJson;
    private UserEntity workerUserEntity;
    private Optional<UserEntity> optionalUserEntity;
    private String loginJson;
    private UserEntity customerUserEntity;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

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
        RoleEntity workerRoleEntity = new RoleEntity(2L, RoleEntityEnum.WAREHOUSE_WORKER, "Wharehouse worker");

        workerUserEntity = UserEntity.builder()
                .id(2L)
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .phoneNumber("+573003216598")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(876436432L)
                .role(workerRoleEntity)
                .enabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .failedAttempts(0)
                .build();

        RoleEntity customerRoleEntity = RoleEntity.builder()
                .id(3L)
                .name(RoleEntityEnum.CUSTOMER)
                .description("Customer")
                .build();

        customerUserEntity = UserEntity.builder()
                .id(3L)
                .firstName("Messi")
                .lastName("Perez")
                .email("messi@email.com")
                .password("jsduiiefw893iu32e8921,mdñ{ñ{.")
                .phoneNumber("+573003219832")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(876436432L)
                .role(customerRoleEntity)
                .enabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .failedAttempts(0)
                .build();

        optionalUserEntity = Optional.of(workerUserEntity);

        loginJson = """
                {
                "username" : "ronaldo@email.com",
                "password" : "password"
                }
                """;
    }

    private static Stream<Arguments> providedWhen_saveWarehouseWorker_expect_statusCreated() {
        return Stream.of(
                Arguments.of("""
                {
                    "firstName": "Ronaldo",
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
                    "lastName": "Ramirez",
                    "identification": 876436432,
                    "phoneNumber": "3003216598",
                    "birthDate": "1990-01-01",
                    "email": "ronaldo@email.com",
                    "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                }
                """)
        );
    }

    @ParameterizedTest
    @MethodSource("providedWhen_saveWarehouseWorker_expect_statusCreated")
    @WithMockUser(username = ADMIN_USERNAME, roles = {ADMIN})
    void when_saveWarehouseWorker_expect_statusCreated(String saveUserJson) throws Exception {

        when(userRepository.save(any(UserEntity.class))).thenReturn(workerUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(saveUserJson))
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
                            "birthDate": "1990/01/01",
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
                        """),
                Arguments.of("""
                        {
                            "firstName": "Ronaldo",
                            "lastName": "Ramirez",
                            "identification": "876436fdishd",
                            "phoneNumber": "+573003216598",
                            "birthDate": "1990-01-01",
                            "email": "user@email.com",
                            "password": "foeofjwoeie"
                        }
                        """)
        );
    }

    @ParameterizedTest
    @MethodSource("providedExpect_badRequest_when_requestValidationFails")
    @WithMockUser(username = ADMIN_USERNAME, roles = {ADMIN})
    void expect_badRequest_when_requestValidationFails(String userJsonBadRequest) throws Exception {

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonBadRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = {ADMIN})
    void expect_conflict_when_userEmailExists() throws Exception {

        when(userRepository.findByEmail(anyString())).thenReturn(optionalUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = {ADMIN})
    void expect_conflict_when_userIdentificationExists() throws Exception {

        when(userRepository.findByIdentification(anyLong())).thenReturn(optionalUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ADMIN)
    void expect_conflict_when_userPhoneNumberExists() throws Exception {

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(optionalUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict());
    }

    @Test
    void when_loginUser_expect_loggedInSuccessfully() throws Exception {

        when(userRepository.findByEmail(anyString())).thenReturn(optionalUserEntity);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);

        mockMvc.perform(post(USER.concat(LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    void expect_forbidden_when_userIsLocked() throws Exception {
        workerUserEntity.setAccountNoLocked(Boolean.FALSE);
        workerUserEntity.setAccountLockedDatetime(Instant.now());

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(workerUserEntity));

        mockMvc.perform(post(USER.concat(LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "2,ronaldo@email.com", roles = "WAREHOUSE_WORKER")
    void expect_forbidden_when_userDoesNotHavePermissions() throws Exception {
        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void expect_unauthorized_when_passwordIsWrong() throws Exception {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(workerUserEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.FALSE);

        mockMvc.perform(post(USER.concat(LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void expect_unauthorized_when_anInvalidTokenWasGiven() throws Exception {
        String fakeToken = "fakeToken";

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
                        .header(HttpHeaders.AUTHORIZATION, BEARER.concat(fakeToken))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void expect_unauthorized_when_anEmptyTokenWasSent() throws Exception {

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_tokenIsValid_expect_successfullyAuthenticatedAndAllowsToSave() throws Exception {
        String username = "1,gustavo.salazar.co@gmail.com";

        String adminEmail = "gustavo.salazar.co@gmail.com";

        String userEmail = "ronaldo@email.com";

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_".concat(ADMIN)));

        String token = jwtUtils.createToken(new UsernamePasswordAuthenticationToken(
                username, "password", simpleGrantedAuthorities
        ));

        UserEntity adminEntity = UserEntity.builder()
                .id(1L)
                .password("password")
                .role(new RoleEntity(1L, RoleEntityEnum.ADMIN, "ADMIN"))
                .enabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        when(userRepository.findByEmail(adminEmail)).thenReturn(Optional.of(adminEntity));

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        when(userRepository.save(any(UserEntity.class))).thenReturn(workerUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP_WAREHOUSE_WORKER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                        .header(HttpHeaders.AUTHORIZATION, BEARER.concat(token))
                )
                .andExpect(status().isCreated());
    }

    private static Stream<Arguments> providedWhen_saveCustomer_expect_statusCreated() {
        return Stream.of(
                Arguments.of("""
                {
                    "firstName": "Messi",
                    "lastName": "Perez",
                    "identification": 876436984,
                    "phoneNumber": "+573003216545",
                    "birthDate": "1990-01-01",
                    "email": "messi@email.com",
                    "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                }
                """),
                Arguments.of("""
                {
                    "firstName": "Messi",
                    "lastName": "Perez",
                    "identification": 876436984,
                    "phoneNumber": "3003216545",
                    "birthDate": "1990-01-01",
                    "email": "messi@email.com",
                    "password": "vfegnkj67rJBEFWIU87Ykjnfderiufe"
                }
                """)
        );
    }

    @ParameterizedTest
    @MethodSource("providedWhen_saveCustomer_expect_statusCreated")
    void when_saveCustomer_expect_statusCreated(String saveUserJson) throws Exception {

        when(userRepository.save(any(UserEntity.class))).thenReturn(customerUserEntity);

        mockMvc.perform(post(USER.concat(SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveUserJson))
                .andExpect(status().isCreated());
    }
}