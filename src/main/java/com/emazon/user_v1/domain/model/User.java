package com.emazon.user_v1.domain.model;

import com.emazon.user_v1.domain.model.builder.IBuilder;

import java.time.LocalDate;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private Long identification;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String password;
    private Role role;

    private Boolean enabled;

    private Boolean accountNoExpired;

    private Boolean accountNoLocked;

    private Boolean credentialNoExpired;

    public static UserBuilder builder() {
        return new User.UserBuilder();
    }

    public static class UserBuilder implements IBuilder<User> {

        private Long id;
        private String firstName;
        private String lastName;
        private Long identification;
        private String phoneNumber;
        private LocalDate birthDate;
        private String email;
        private String password;
        private Role role;

        private Boolean enabled;

        private Boolean accountNoExpired;

        private Boolean accountNoLocked;

        private Boolean credentialNoExpired;

        public UserBuilder() {
            this.enabled = true;
            this.accountNoExpired = true;
            this.accountNoLocked = true;
            this.credentialNoExpired = true;
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder identification(Long identification) {
            this.identification = identification;
            return this;
        }

        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder accountNoExpired(Boolean accountNoExpired) {
            this.accountNoExpired = accountNoExpired;
            return this;
        }

        public UserBuilder accountNoLocked(Boolean accountNoLocked) {
            this.accountNoLocked = accountNoLocked;
            return this;
        }

        public UserBuilder credentialNoExpired(Boolean credentialNoExpired) {
            this.credentialNoExpired = credentialNoExpired;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setIdentification(identification);
            user.setPhoneNumber(phoneNumber);
            user.setBirthDate(birthDate);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            user.setEnabled(enabled);
            user.setAccountNoExpired(accountNoExpired);
            user.setAccountNoLocked(accountNoLocked);
            user.setCredentialNoExpired(credentialNoExpired);
            return user;
        }
    }

    public User() {
        this.enabled = true;
        this.accountNoExpired = true;
        this.accountNoLocked = true;
        this.credentialNoExpired = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getIdentification() {
        return identification;
    }

    public void setIdentification(Long identification) {
        this.identification = identification;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAccountNoExpired() {
        return accountNoExpired;
    }

    public void setAccountNoExpired(Boolean accountNoExpired) {
        this.accountNoExpired = accountNoExpired;
    }

    public Boolean getAccountNoLocked() {
        return accountNoLocked;
    }

    public void setAccountNoLocked(Boolean accountNoLocked) {
        this.accountNoLocked = accountNoLocked;
    }

    public Boolean getCredentialNoExpired() {
        return credentialNoExpired;
    }

    public void setCredentialNoExpired(Boolean credentialNoExpired) {
        this.credentialNoExpired = credentialNoExpired;
    }
}
