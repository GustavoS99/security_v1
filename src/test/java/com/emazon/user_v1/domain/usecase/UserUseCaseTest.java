package com.emazon.user_v1.domain.usecase;

import com.emazon.user_v1.domain.auth.IAuthenticationPort;
import com.emazon.user_v1.domain.exception.*;
import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.domain.spi.IRolePersistencePort;
import com.emazon.user_v1.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
}