package com.emazon.user_v1.application.mapper;

import com.emazon.user_v1.application.dto.LoginResponse;
import com.emazon.user_v1.domain.model.Login;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginResponseMapper {

    LoginResponse toLoginResponse(Login login);
}
