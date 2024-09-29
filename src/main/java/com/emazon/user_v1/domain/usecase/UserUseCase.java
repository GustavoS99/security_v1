package com.emazon.user_v1.domain.usecase;

import com.emazon.user_v1.domain.api.IUserServicePort;
import com.emazon.user_v1.domain.auth.IAuthenticationPort;
import com.emazon.user_v1.domain.exception.*;
import com.emazon.user_v1.domain.model.Login;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.domain.spi.IRolePersistencePort;
import com.emazon.user_v1.domain.spi.IUserPersistencePort;

import java.time.*;
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

    private void save(User user) {
        validateInputs(user);

        user.setPassword(authenticationPort.encode(user.getPassword()));

        userPersistencePort.save(user);
    }

    private void validateInputs(User user) {
        user.setFirstName(validateStringInput(user.getFirstName()));

        user.setLastName(validateStringInput(user.getLastName()));

        user.setPassword(validateStringInput(user.getPassword()));

        validateEmail(validateStringInput(user.getEmail()));

        validatePhoneNumber(validateStringInput(user.getPhoneNumber()));

        validateIdentification(user.getIdentification());

        validateIsAdult(user.getBirthDate());

    }

    private String validateStringInput(String string) {
        if(string == null || string.trim().isEmpty())
            throw new EmptyUserAttributeException();
        return string.trim();
    }

    private void validateIdentification(Long identification) {
        if(identification == null || identification == 0)
            throw new EmptyUserAttributeException();

        if(userPersistencePort.findByIdentification(identification).isPresent()) {
            throw new IdentificationExistsException();
        }
    }

    public void validateEmail(String email) {

        if(!Pattern.compile(EMAIL_REGEX).matcher(email).matches())
            throw new InvalidEmailException();

        if(userPersistencePort.findByEmail(email).isPresent())
            throw new EmailExistsException();
    }

    private void validatePhoneNumber(String phoneNumber) {

        if (!Pattern.compile(PHONE_NUMBER_REGEX).matcher(phoneNumber).matches())
            throw new InvalidPhoneNumberException();

        if (userPersistencePort.findByPhoneNumber(phoneNumber).isPresent())
            throw new PhoneNumberExistsException();
    }

    private void validateIsAdult(LocalDate birthDate) {
        if(birthDate == null)
            throw new EmptyUserAttributeException();

        if(Period.between(birthDate, LocalDate.now()).getYears() < EIGHTEEN_YEARS)
            throw new InvalidAgeException();
    }

    @Override
    public void saveWarehouseWorker(User user) {
        user.setRole(rolePersistencePort.findByName(RoleEnum.WAREHOUSE_WORKER.name())
                .orElseThrow(SystemRoleNotFoundException::new));

        save(user);
    }

    @Override
    public Login authenticate(Login login) {
        User user = userPersistencePort.findByEmail(login.getUsername()).orElseThrow(UserNotFoundException::new);

        validateLockedAccount(user);

        validateCredentials(login, user);

        authenticationPort.authenticate(login);

        resetFailedAttempts(user);

        login.setToken(authenticationPort.getToken(user));

        return login;
    }

    private void validateLockedAccount(User user) {
        if(Boolean.FALSE.equals(user.getAccountNoLocked()) && isLockExpired(user)){
            resetFailedAttempts(user);
            return;
        }
        if(Boolean.FALSE.equals(user.getAccountNoLocked()))
            throw new UserLockedException();
    }

    private static boolean isLockExpired(User user) {
        if(user.getAccountLockedDatetime() == null)
            return Boolean.TRUE;
        Instant accountLockedDatetime = user.getAccountLockedDatetime();
        Instant now = Instant.now();
        Duration between = Duration.between(accountLockedDatetime, now);
        return between.toHours() >= HOURS_UNTIL_LOCKING_EXPIRATION;
    }

    private void validateCredentials(Login login, User user) {
        if(Boolean.FALSE.equals(authenticationPort.matches(login.getPassword(), user.getPassword()))) {
            increaseFailedAttempts(user);
            setLockUserAccount(user);
            userPersistencePort.save(user);
            throw new BadUserCredentialsException(getAvailableAttempts(user).toString());
        }
    }

    private void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempts() + FAIL_ATTEMPT;
        user.setFailedAttempts(newFailAttempts);
    }

    private Integer getAvailableAttempts(User user) {
        return MAX_FAILED_ATTEMPTS - user.getFailedAttempts();
    }

    private void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        user.setAccountNoLocked(true);
        user.setAccountLockedDatetime(null);
        userPersistencePort.save(user);
    }

    private void setLockUserAccount(User user) {
        if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.setAccountNoLocked(false);
            user.setAccountLockedDatetime(Instant.now());
        }
    }

    @Override
    public void saveCustomer(User user) {
        user.setRole(rolePersistencePort.findByName(RoleEnum.CUSTOMER.name())
                .orElseThrow(SystemRoleNotFoundException::new));

        save(user);
    }
}
