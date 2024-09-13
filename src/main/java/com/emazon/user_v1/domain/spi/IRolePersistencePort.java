package com.emazon.user_v1.domain.spi;

import com.emazon.user_v1.domain.model.Role;

import java.util.Optional;

public interface IRolePersistencePort {
    Optional<Role> findByName(String name);
}
