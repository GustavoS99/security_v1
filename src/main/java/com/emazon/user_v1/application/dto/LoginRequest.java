package com.emazon.user_v1.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.emazon.user_v1.util.GlobalConstants.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {
    @NotNull(message = EMPTY_EMAIL_USER_MESSAGE)
    @NotEmpty(message = EMPTY_EMAIL_USER_MESSAGE)
    @Size(min = MIN_LEN_EMAIL_USER, max = MAX_LEN_EMAIL_USER, message = INVALID_EMAIL_USER_LENGTH_MESSAGE)
    @Email(regexp = EMAIL_REGEX, message = INVALID_EMAIL_USER_MESSAGE)
    private String username;

    @NotNull(message = EMPTY_PASSWORD_MESSAGE)
    @NotEmpty(message = EMPTY_PASSWORD_MESSAGE)
    private String password;
}
