package com.emazon.user_v1.domain.api;

import com.emazon.user_v1.domain.model.Login;
import com.emazon.user_v1.domain.model.User;

public interface IUserServicePort {

    void saveWarehouseWorker(User user);

    Login authenticate(Login login);

    void saveCustomer(User user);
}
