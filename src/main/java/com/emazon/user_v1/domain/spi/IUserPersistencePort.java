package com.emazon.user_v1.domain.spi;

import com.emazon.user_v1.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByIdentification(Long identification);

    Optional<User> findById(Long id);
}
