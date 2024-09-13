package com.emazon.user_v1.domain.auth;

public interface IAuthenticationPort {

    String encode(String password);
}
