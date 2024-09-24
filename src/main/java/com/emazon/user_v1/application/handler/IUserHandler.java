package com.emazon.user_v1.application.handler;

import com.emazon.user_v1.application.dto.LoginRequest;
import com.emazon.user_v1.application.dto.LoginResponse;
import com.emazon.user_v1.application.dto.UserRequest;

public interface IUserHandler {

    void saveWarehouseWorker(UserRequest userRequest);

    LoginResponse authenticate(LoginRequest loginRequest);
}
