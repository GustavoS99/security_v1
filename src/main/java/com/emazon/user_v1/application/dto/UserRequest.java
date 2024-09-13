package com.emazon.user_v1.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import static com.emazon.user_v1.util.GlobalConstants.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull(message = EMPTY_FIRSTNAME_USER_MESSAGE)
    @NotEmpty(message = EMPTY_FIRSTNAME_USER_MESSAGE)
    @Size(min = MIN_LEN_FIRSTNAME_USER, max = MAX_LEN_FIRSTNAME_USER, message = INVALID_FIRSTNAME_USER_LENGTH_MESSAGE)
    private String firstName;

    @NotNull(message = EMPTY_LASTNAME_USER_MESSAGE)
    @NotEmpty(message = EMPTY_LASTNAME_USER_MESSAGE)
    @Size(min = MIN_LEN_LASTNAME_USER, max = MAX_LEN_LASTNAME_USER, message = INVALID_LASTNAME_USER_LENGTH_MESSAGE)
    private String lastName;

    @NotNull(message = EMPTY_IDENTIFICATION_USER_MESSAGE)
    private Long identification;

    @NotNull(message = EMPTY_PHONE_NUMBER_USER_MESSAGE)
    @NotEmpty(message = EMPTY_PHONE_NUMBER_USER_MESSAGE)
    @Size(
            min = MIN_LEN_PHONE_NUMBER_USER,
            max = MAX_LEN_PHONE_NUMBER_USER,
            message = INVALID_PHONE_NUMBER_USER_LENGTH_MESSAGE
    )
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = INVALID_PHONE_NUMBER_MESSAGE)
    private String phoneNumber;

    @NotNull(message = EMPTY_BIRTH_DATE_USER_MESSAGE)
    @NotEmpty(message = EMPTY_BIRTH_DATE_USER_MESSAGE)
    @Pattern(regexp = DATE_REGEX, message = INVALID_BIRTH_DATE_USER_MESSAGE)
    private String birthDate;

    @NotNull(message = EMPTY_EMAIL_USER_MESSAGE)
    @NotEmpty(message = EMPTY_EMAIL_USER_MESSAGE)
    @Size(min = MIN_LEN_EMAIL_USER, max = MAX_LEN_EMAIL_USER, message = INVALID_EMAIL_USER_LENGTH_MESSAGE)
    @Email(regexp = EMAIL_REGEX, message = INVALID_EMAIL_USER_MESSAGE)
    private String email;

    @NotNull(message = EMPTY_PASSWORD_MESSAGE)
    @NotEmpty(message = EMPTY_PASSWORD_MESSAGE)
    @Size(
            min = MIN_LEN_PASSWORD_USER,
            max = MAX_LEN_PASSWORD_USER,
            message = INVALID_PASSWORD_LENGTH_MESSAGE
    )
    private String password;
}
