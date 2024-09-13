package com.emazon.user_v1.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEntityEnum name;

    @Column(length = 150,
            unique = true,
            nullable = false)
    private String description;
}
