package com.emazon.user_v1.domain.usecase;

import com.emazon.user_v1.domain.auth.IAuthenticationPort;
import com.emazon.user_v1.domain.exception.*;
import com.emazon.user_v1.domain.model.Login;
import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.domain.spi.IRolePersistencePort;
import com.emazon.user_v1.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

import static com.emazon.user_v1.util.GlobalConstants.HOURS_UNTIL_LOCKING_EXPIRATION;
import static com.emazon.user_v1.util.GlobalConstants.MAX_FAILED_ATTEMPTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IRolePersistencePort rolePersistencePort;

    @Mock
    private IAuthenticationPort authenticationPort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User workerUser;
    private Role wharehousRole;
    private Login login;
    private Role customerRole;
    private User customerUser;

    @BeforeEach
    void setUp() {

        wharehousRole = new Role(2L, RoleEnum.WAREHOUSE_WORKER, "Wharehouse worker");

        workerUser = User.builder()
                .id(2L)
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .phoneNumber("+573003216598")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(876436432L)
                .build();


        login = Login.builder()
                .username("test@mail.com")
                .password("12234")
                .build();

        customerUser = User.builder()
                .id(3L)
                .firstName("Messi")
                .lastName("Perez")
                .email("messi@email.com")
                .password("kvuikfreiueuhkjnwek8793458745knferu88732")
                .phoneNumber("3003125648")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(34981356L)
                .build();

        customerRole = Role.builder()
                .id(3L)
                .name(RoleEnum.CUSTOMER)
                .description("Customer")
                .build();
    }

    @Test
    void when_saveWarehouseWorker_expect_callToPersistence() {

        String encodedPassword = "oifjeoif9ao784i32r487ihewaii";

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByIdentification(anyLong())).thenReturn(Optional.empty());

        when(authenticationPort.encode(anyString())).thenReturn(encodedPassword);

        when(userPersistencePort.save(any(User.class))).thenReturn(workerUser);

        userUseCase.saveWarehouseWorker(workerUser);

        verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "user@com",
            "user@domain..com",
            "@example.com",
            "user@.com"
    })
    void expect_InvalidEmailException_when_emailPatternIsWrong(String email) {
        workerUser.setEmail(email);

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        assertThrows(InvalidEmailException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @Test
    void expect_EmailExistsException_when_emailExists() {

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.of(workerUser));

        assertThrows(EmailExistsException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "++1234567890",
            "12345",
            "1234567890123456",
            "+571234567890123456",
            "+1234abc567"
    })
    void expect_InvalidPhoneNumberException_when_phoneNumberPatternIsWrong(String phoneNumber) {
        workerUser.setPhoneNumber(phoneNumber);

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidPhoneNumberException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @Test
    void expect_PhoneNumberExistsException_when_phoneNumberExists() {
        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByPhoneNumber(anyString())).thenReturn(Optional.of(workerUser));

        assertThrows(PhoneNumberExistsException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @Test
    void expect_IdentificationExistsException_when_identificationExists() {
        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByIdentification(anyLong())).thenReturn(Optional.of(workerUser));

        assertThrows(IdentificationExistsException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @Test
    void expect_InvalidAgeException_when_userIsYounger() {
        workerUser.setBirthDate(LocalDate.now().minusYears(1));

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByIdentification(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidAgeException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    private static Stream<Arguments> providedExpect_EmptyUserAttributeException_when_anyInputIsEmpty() {
        return Stream.of(
                Arguments.of("setFirstName", " "),
                Arguments.of("setFirstName", null),
                Arguments.of("setLastName", " "),
                Arguments.of("setLastName", null),
                Arguments.of("setPhoneNumber", " "),
                Arguments.of("setPhoneNumber", null),
                Arguments.of("setPassword", " "),
                Arguments.of("setPassword", null),
                Arguments.of("setEmail", " "),
                Arguments.of("setEmail", null)
        );
    }

    @ParameterizedTest
    @MethodSource("providedExpect_EmptyUserAttributeException_when_anyInputIsEmpty")
    void expect_EmptyUserAttributeException_when_anyInputIsEmpty(String methodName, String empty) throws Exception{
        var method = workerUser.getClass().getMethod(methodName, String.class);

        method.invoke(workerUser, empty);

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        assertThrows(EmptyUserAttributeException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0",
            "null"
    }, nullValues = {"null"})
    void expect_EmptyUserAttributeException_when_identificationIsEmpty(Long identification) {
        workerUser.setIdentification(identification);

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        assertThrows(EmptyUserAttributeException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @Test
    void expect_EmptyUserAttributeException_when_birthDateIsEmpty() {
        workerUser.setBirthDate(null);

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(wharehousRole));

        assertThrows(EmptyUserAttributeException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }

    @Test
    void when_authenticate_expect_successfullyAccess() {

        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.of(workerUser));
        when(authenticationPort.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);
        when(authenticationPort.getToken(any(User.class))).thenReturn(expectedToken);

        Login result = userUseCase.authenticate(login);

        assertNotNull(result);
        assertEquals(expectedToken, result.getToken());
    }

    @Test
    void when_authenticateExpiredLockUser_expect_successfullyAccess() {

        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        workerUser.setAccountNoLocked(Boolean.FALSE);
        workerUser.setAccountLockedDatetime(Instant.now().minus(HOURS_UNTIL_LOCKING_EXPIRATION, ChronoUnit.HOURS));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.of(workerUser));
        when(authenticationPort.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);
        when(authenticationPort.getToken(any(User.class))).thenReturn(expectedToken);

        Login result = userUseCase.authenticate(login);

        assertNotNull(result);
        assertEquals(expectedToken, result.getToken());
    }

    @Test
    void expect_UserLockedException_when_userIsLocked() {
        workerUser.setAccountNoLocked(Boolean.FALSE);
        workerUser.setAccountLockedDatetime(Instant.now());

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.of(workerUser));

        assertThrows(UserLockedException.class, () -> userUseCase.authenticate(login));
    }

    @Test
    void expect_UserNotFoundException_when_userDoesNotExist() {
        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userUseCase.authenticate(login));
    }

    @Test
    void expect_BadUserCredentialsException_when_passwordDoesNotMatch() {
        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.of(workerUser));
        when(authenticationPort.matches(anyString(), anyString())).thenReturn(Boolean.FALSE);

        assertThrows(BadUserCredentialsException.class, () -> userUseCase.authenticate(login));
    }

    @Test
    void expect_BadUserCredentialsExceptionAndLockUser_when_passwordDoesNotMatchAndFailedAttemptsExceedLimit() {
        workerUser.setFailedAttempts(MAX_FAILED_ATTEMPTS);

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.of(workerUser));
        when(authenticationPort.matches(anyString(), anyString())).thenReturn(Boolean.FALSE);

        assertThrows(BadUserCredentialsException.class, () -> userUseCase.authenticate(login));
        verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @Test
    void when_saveCustomer_expect_callToPersistence() {

        String encodedPassword = "oifjeoif9ao784i32r487ihewaii";

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.of(customerRole));

        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        when(userPersistencePort.findByIdentification(anyLong())).thenReturn(Optional.empty());

        when(authenticationPort.encode(anyString())).thenReturn(encodedPassword);

        when(userPersistencePort.save(any(User.class))).thenReturn(customerUser);

        userUseCase.saveWarehouseWorker(workerUser);

        verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @Test
    void expect_SystemRoleNotFoundException_when_roleNameDoesNotExist() {

        when(rolePersistencePort.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(SystemRoleNotFoundException.class, () -> userUseCase.saveWarehouseWorker(workerUser));
    }
}