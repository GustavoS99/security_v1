package com.emazon.user_v1.infrastructure.out.jpa.adapter;

import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.domain.spi.IRolePersistencePort;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntityEnum;
import com.emazon.user_v1.infrastructure.out.jpa.mapper.RoleEntityMapper;
import com.emazon.user_v1.infrastructure.out.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(RoleEntityEnum.valueOf(name)).map(roleEntityMapper::toRole);
    }
}
