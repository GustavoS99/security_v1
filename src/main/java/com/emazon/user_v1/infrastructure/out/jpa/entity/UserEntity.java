package com.emazon.user_v1.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.Serial;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private Long identification;

    @Column(length = 13, nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(name = "account_no_expired", nullable = false)
    private Boolean accountNoExpired;

    @Column(name = "account_no_locked", nullable = false)
    private Boolean accountNoLocked;

    @Column(name = "credential_no_expired", nullable = false)
    private Boolean credentialNoExpired;

    @Column(name = "failed_attempts", nullable = false)
    private Integer failedAttempts;

    @Column(name = "account_locked_datetime")
    private Instant accountLockedDatetime;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(
                new SimpleGrantedAuthority("ROLE_".concat(role.getName().name()))
        );
        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNoExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNoLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialNoExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
