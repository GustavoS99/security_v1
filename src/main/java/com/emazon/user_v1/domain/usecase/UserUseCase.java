package com.emazon.user_v1.domain.usecase;

import com.emazon.user_v1.domain.api.IUserServicePort;
import com.emazon.user_v1.domain.auth.IAuthenticationPort;
import com.emazon.user_v1.domain.exception.*;
import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.domain.spi.IRolePersistencePort;
import com.emazon.user_v1.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import static com.emazon.user_v1.util.GlobalConstants.*;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IAuthenticationPort authenticationPort;

    public UserUseCase(
            IUserPersistencePort userPersistencePort,
            IRolePersistencePort rolePersistencePort,
            IAuthenticationPort authenticationPort
    ) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
        this.authenticationPort = authenticationPort;
    }

    @Override
    public void save(User user) {
        validateInputs(user);

        user.setPassword(authenticationPort.encode(user.getPassword()));

        userPersistencePort.save(user);
    }

    private void validateInputs(User user) {
        user.setFirstName(validateStringInput(user.getFirstName()));

        user.setLastName(validateStringInput(user.getLastName()));

        user.setPassword(validateStringInput(user.getPassword()));

        validateEmail(user.getEmail());

        validatePhoneNumber(user.getPhoneNumber());

        validateIdentification(user.getIdentification());

        validateIsAdult(user.getBirthDate());

    }

    private String validateStringInput(String string) {
        if(string.trim().isEmpty())
            throw new EmptyUserAttributeException();
        return string.trim();
    }

    private void validateIdentification(Long identification) {
        if(userPersistencePort.findByIdentification(identification).isPresent()) {
            throw new IdentificationExistsException();
        }
    }

    public void validateEmail(String email) {
        email = validateStringInput(email);

        if(!Pattern.compile(EMAIL_REGEX).matcher(email).matches())
            throw new InvalidEmailException();

        if(userPersistencePort.findByEmail(email).isPresent())
            throw new EmailExistsException();
    }

    private void validatePhoneNumber(String phoneNumber) {
        phoneNumber = validateStringInput(phoneNumber);

        if (!Pattern.compile(PHONE_NUMBER_REGEX).matcher(phoneNumber).matches())
            throw new InvalidPhoneNumberException();

        if (userPersistencePort.findByPhoneNumber(phoneNumber).isPresent())
            throw new PhoneNumberExistsException();
    }

    private void validateIsAdult(LocalDate birthDate) {
        if(Period.between(birthDate, LocalDate.now()).getYears() < EIGHTEEN_YEARS)
            throw new InvalidAgeException();
    }

    @Override
    public void saveWarehouseWorker(User user) {

        Role role = rolePersistencePort.findByName(RoleEnum.WAREHOUSE_WORKER.name()).orElseThrow();

        user.setRole(role);

        save(user);
    }
}
