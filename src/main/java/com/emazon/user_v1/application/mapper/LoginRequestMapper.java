package com.emazon.user_v1.application.mapper;

import com.emazon.user_v1.application.dto.LoginRequest;
import com.emazon.user_v1.domain.model.Login;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginRequestMapper {

    @Mapping(target = "token", ignore = true)
    Login toLogin(LoginRequest loginRequest);
}
