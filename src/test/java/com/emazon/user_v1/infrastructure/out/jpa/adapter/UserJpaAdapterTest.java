package com.emazon.user_v1.infrastructure.out.jpa.adapter;

import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntity;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntityEnum;
import com.emazon.user_v1.infrastructure.out.jpa.entity.UserEntity;
import com.emazon.user_v1.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    private UserEntity userEntity;
    private User user;
    private Optional<UserEntity> userEntityOptional;

    @BeforeEach
    void setUp() {

        Role role = new Role(2L, RoleEnum.WAREHOUSE_WORKER, "Wharehouse worker");

        user = User.builder()
                .id(2L)
                .firstName("Ronaldo")
                .lastName("Ramirez")
                .email("ronaldo@email.com")
                .password("vfegnkj67rJBEFWIU87Ykjnfderiufe")
                .phoneNumber("+573003216598")
                .birthDate(LocalDate.of(1990, 1, 1))
                .identification(876436432L)
                .role(role)
                .build();

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

        userEntityOptional = Optional.of(userEntity);

        when(userEntityMapper.toUser(any(UserEntity.class))).thenReturn(user);
    }

    @Test
    void when_save_expect_callToRepository() {
        when(userEntityMapper.toUserEntity(any(User.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User result = userJpaAdapter.save(user);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void when_findByEmail_expect_callToRepository() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntityOptional);

        Optional<User> result = userJpaAdapter.findByEmail(user.getEmail());

        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void when_findByPhoneNumber_expect_callToRepository() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(userEntityOptional);

        Optional<User> result = userJpaAdapter.findByPhoneNumber(user.getPhoneNumber());

        assertNotNull(result);
        verify(userRepository, times(1)).findByPhoneNumber(anyString());
    }

    @Test
    void when_findByIdentification_expect_callToRepository() {
        when(userRepository.findByIdentification(anyLong())).thenReturn(userEntityOptional);

        Optional<User> result = userJpaAdapter.findByIdentification(user.getIdentification());

        assertNotNull(result);
        verify(userRepository, times(1)).findByIdentification(anyLong());
    }

    @Test
    void when_findById_expect_callToRepository() {
        when(userRepository.findById(anyLong())).thenReturn(userEntityOptional);

        Optional<User> result = userJpaAdapter.findById(user.getId());

        assertNotNull(result);
        verify(userRepository, times(1)).findById(anyLong());
    }
}