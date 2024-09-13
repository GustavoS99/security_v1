package com.emazon.user_v1.application.handler;

import com.emazon.user_v1.application.dto.UserRequest;
import com.emazon.user_v1.application.mapper.UserRequestMapper;
import com.emazon.user_v1.domain.api.IUserServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final UserRequestMapper userRequestMapper;

    @Override
    public void saveWarehouseWorker(UserRequest userRequest) {
        userServicePort.saveWarehouseWorker(userRequestMapper.toUser(userRequest));
    }
}
