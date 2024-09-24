package com.emazon.user_v1.util;

public class GlobalConstants {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+$";

    public static final String PHONE_NUMBER_REGEX = "^\\+?\\d{10,13}$";

    public static final String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public static final int EIGHTEEN_YEARS = 18;

    public static final String USER_NOT_FOUND = "The user was not found";

    public static final int ID_USER_INDEX = 0;

    public static final int EMAIL_USER_INDEX = 1;

    public static final String EMPTY_PASSWORD_MESSAGE = "Password cannot be empty";

    public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "Password must be between 7 and 255 characters";

    public static final String EMPTY_FIRSTNAME_USER_MESSAGE = "Firstname cannot be empty";

    public static final String INVALID_FIRSTNAME_USER_LENGTH_MESSAGE = "Firstname must be between 1 and 255 characters";

    public static final String EMPTY_LASTNAME_USER_MESSAGE = "Lastname cannot be empty";

    public static final String INVALID_LASTNAME_USER_LENGTH_MESSAGE = "Lastname must be between 1 and 255 characters";

    public static final String EMPTY_EMAIL_USER_MESSAGE = "Email cannot be empty";

    public static final String INVALID_EMAIL_USER_LENGTH_MESSAGE = "Email must be between 1 and 255 characters";

    public static final String EMPTY_PHONE_NUMBER_USER_MESSAGE = "Phone number cannot be empty";

    public static final String EMPTY_IDENTIFICATION_USER_MESSAGE = "Identification number cannot be empty";

    public static final String INVALID_PHONE_NUMBER_USER_LENGTH_MESSAGE = "Phone number must be between 1 and 255 characters";

    public static final String EMPTY_BIRTH_DATE_USER_MESSAGE = "Birth date cannot be empty";

    public static final String INVALID_BIRTH_DATE_USER_MESSAGE = "Birth date must be a valid date. Valid format YYYY-MM-DD";

    public static final int MIN_LEN_FIRSTNAME_USER = 1;

    public static final int MAX_LEN_FIRSTNAME_USER = 255;

    public static final int MIN_LEN_LASTNAME_USER = 1;

    public static final int MAX_LEN_LASTNAME_USER = 255;

    public static final int MIN_LEN_PHONE_NUMBER_USER = 10;

    public static final int MAX_LEN_PHONE_NUMBER_USER = 13;

    public static final int MIN_LEN_EMAIL_USER = 7;

    public static final int MAX_LEN_EMAIL_USER = 255;

    public static final int MIN_LEN_PASSWORD_USER = 7;

    public static final int MAX_LEN_PASSWORD_USER = 255;

    public static final String INVALID_EMAIL_USER_MESSAGE = "Invalid email pattern";

    public static final String INVALID_PHONE_NUMBER_MESSAGE = "Invalid phone number pattern";

    public static final String BEARER = "Bearer ";

    public static final Integer MAX_FAILED_ATTEMPTS = 3;

    public static final String ADMIN = "ADMIN";

    public static final long HOURS_UNTIL_LOCKING_EXPIRATION = 24;

    public static final Integer FAIL_ATTEMPT = 1;

    private GlobalConstants() {}
}
