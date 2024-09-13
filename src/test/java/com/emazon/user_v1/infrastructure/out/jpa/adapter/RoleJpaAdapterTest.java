package com.emazon.user_v1.infrastructure.out.jpa.adapter;

import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.model.RoleEnum;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntity;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntityEnum;
import com.emazon.user_v1.infrastructure.out.jpa.mapper.RoleEntityMapper;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleJpaAdapterTest {

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private RoleEntityMapper roleEntityMapper;

    @InjectMocks
    private RoleJpaAdapter roleJpaAdapter;

    @Test
    void when_findByName_expect_callToRepository() {
        Role role = new Role(2L, RoleEnum.WAREHOUSE_WORKER, "Wharehouse worker");
        RoleEntity roleEntity = new RoleEntity(2L, RoleEntityEnum.WAREHOUSE_WORKER, "Wharehouse worker");

        when(roleRepository.findByName(any(RoleEntityEnum.class))).thenReturn(Optional.of(roleEntity));
        when(roleEntityMapper.toRole(any(RoleEntity.class))).thenReturn(role);

        Optional<Role> result = roleJpaAdapter.findByName(role.getName().name());

        assertNotNull(result);
        verify(roleRepository, times(1)).findByName(any(RoleEntityEnum.class));
    }
}