package com.emazon.user_v1.infrastructure.out.jpa.repository;

import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntity;
import com.emazon.user_v1.infrastructure.out.jpa.entity.RoleEntityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleEntityEnum name);
}
