package com.emazon.user_v1.domain.auth;

import com.emazon.user_v1.domain.model.Login;
import com.emazon.user_v1.domain.model.User;

public interface IAuthenticationPort {

    String encode(String password);

    Boolean matches(String password, String encodedPassword);

    String getToken(User user);

    void authenticate(Login login);
}
