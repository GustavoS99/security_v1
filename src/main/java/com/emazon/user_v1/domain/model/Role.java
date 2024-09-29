package com.emazon.user_v1.domain.model;

import com.emazon.user_v1.domain.model.builder.IBuilder;

public class Role {
    private Long id;

    private RoleEnum name;

    private String description;

    public static RoleBuilder builder() {
        return new RoleBuilder();
    }

    public static class RoleBuilder implements IBuilder<Role> {
        private Long id;

        private RoleEnum name;

        private String description;

        public RoleBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RoleBuilder name(RoleEnum name) {
            this.name = name;
            return this;
        }

        public RoleBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public Role build() {
            Role role = new Role();

            role.setId(id);

            role.setName(name);

            role.setDescription(description);

            return role;
        }
    }

    public Role() {
    }

    public Role(Long id, RoleEnum name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
