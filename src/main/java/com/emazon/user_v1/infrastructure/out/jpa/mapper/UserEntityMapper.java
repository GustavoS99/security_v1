package com.emazon.user_v1.infrastructure.out.jpa.mapper;

import com.emazon.user_v1.domain.model.User;
import com.emazon.user_v1.infrastructure.out.jpa.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleEntityMapper.class})
public interface UserEntityMapper {

    UserEntity toUserEntity(User user);

    User toUser(UserEntity userEntity);
}
