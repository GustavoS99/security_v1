package com.emazon.user_v1.application.mapper;

import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountNoExpired", ignore = true)
    @Mapping(target = "accountNoLocked", ignore = true)
    @Mapping(target = "credentialNoExpired", ignore = true)
    User toUser(UserRequest userRequest);
}
