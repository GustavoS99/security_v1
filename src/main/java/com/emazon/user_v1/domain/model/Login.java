package com.emazon.user_v1.domain.model;

import com.emazon.user_v1.domain.model.builder.IBuilder;

public class Login {
    private String username;
    private String password;
    private String token;

    public static Login.LoginBuilder builder() {
        return new Login.LoginBuilder();
    }

    public static class LoginBuilder implements IBuilder<Login> {
        private String username;
        private String password;
        private String token;

        public LoginBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginBuilder password(String password) {
            this.password = password;
            return this;
        }

        public LoginBuilder token(String token) {
            this.token = token;
            return this;
        }

        @Override
        public Login build() {
            Login login = new Login();
            login.setUsername(username);
            login.setPassword(password);
            login.setToken(token);
            return login;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
