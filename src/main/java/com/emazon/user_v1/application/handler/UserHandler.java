package com.emazon.user_v1.application.handler;

import com.emazon.user_v1.application.dto.LoginRequest;
import com.emazon.user_v1.application.dto.LoginResponse;
import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.application.mapper.LoginRequestMapper;
import com.emazon.user_v1.application.mapper.LoginResponseMapper;
import com.emazon.user_v1.application.mapper.UserRequestMapper;
import com.emazon.user_v1.domain.api.IUserServicePort;
import com.emazon.user_v1.domain.exception.BadUserCredentialsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final UserRequestMapper userRequestMapper;
    private final LoginRequestMapper loginRequestMapper;
    private final LoginResponseMapper loginResponseMapper;

    @Override
    public void saveWarehouseWorker(UserRequest userRequest) {
        userServicePort.saveWarehouseWorker(userRequestMapper.toUser(userRequest));
    }

    @Override
    @Transactional(dontRollbackOn = BadUserCredentialsException.class)
    public LoginResponse authenticate(LoginRequest loginRequest) {
        return loginResponseMapper.toLoginResponse(
                userServicePort.authenticate(loginRequestMapper.toLogin(loginRequest)));
    }

    @Override
    public void saveCustomer(UserRequest userRequest) {
        userServicePort.saveCustomer(userRequestMapper.toUser(userRequest));
    }
}
