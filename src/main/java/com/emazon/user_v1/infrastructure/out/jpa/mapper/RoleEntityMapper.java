package com.emazon.user_v1.infrastructure.out.jpa.mapper;

import com.emazon.user_v1.domain.model.Role;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {

    Role toRole(RoleEntity roleEntity);

    RoleEntity toRoleEntity(Role role);
}
