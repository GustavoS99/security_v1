package com.emazon.user_v1.infrastructure.out.jpa.adapter;

import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.domain.spi.IUserPersistencePort;
import com.emazon.user_v1.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User save(User user) {
        return userEntityMapper.toUser(
                userRepository.save(userEntityMapper.toUserEntity(user)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userEntityMapper::toUser);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).map(userEntityMapper::toUser);
    }

    @Override
    public Optional<User> findByIdentification(Long identification) {
        return userRepository.findByIdentification(identification).map(userEntityMapper::toUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(userEntityMapper::toUser);
    }
}
