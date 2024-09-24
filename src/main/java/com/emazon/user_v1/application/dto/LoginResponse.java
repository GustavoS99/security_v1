package com.emazon.user_v1.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
}
