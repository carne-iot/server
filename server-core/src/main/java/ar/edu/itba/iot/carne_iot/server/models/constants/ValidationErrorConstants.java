package ar.edu.itba.iot.carne_iot.server.models.constants;


import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;

import static ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError.ErrorCause.ILLEGAL_VALUE;
import static ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError.ErrorCause.MISSING_VALUE;

/**
 * Class containing {@link ValidationError} constants to be reused.
 */
public class ValidationErrorConstants {

    public static final ValidationError MISSING_FULL_NAME = new ValidationError(MISSING_VALUE, "fullName",
            "The fullName is missing.");
    public static final ValidationError FULL_NAME_TOO_SHORT = new ValidationError(ILLEGAL_VALUE, "fullName",
            "The fullName is too short.");
    public static final ValidationError FULL_NAME_TOO_LONG = new ValidationError(ILLEGAL_VALUE, "fullName",
            "The fullName is too long.");


    public static final ValidationError MISSING_BIRTH_DATE = new ValidationError(MISSING_VALUE, "birthDate",
            "The birthDate is missing.");
    public static final ValidationError BIRTH_DATE_TOO_LONG_AGO = new ValidationError(ILLEGAL_VALUE, "birthDate",
            "The birthDate is too long ago.");
    public static final ValidationError FUTURE_BIRTH_DATE = new ValidationError(ILLEGAL_VALUE, "birthDate",
            "The birthDate is in the future.");
    public static final ValidationError TOO_YOUNG_USER = new ValidationError(ILLEGAL_VALUE, "birthDate",
            "The birthDate is too actual (i.e user must be older).");


    public static final ValidationError MISSING_USERNAME = new ValidationError(MISSING_VALUE, "username",
            "The username is missing.");
    public static final ValidationError USERNAME_TOO_SHORT = new ValidationError(ILLEGAL_VALUE, "username",
            "The username is too short.");
    public static final ValidationError USERNAME_TOO_LONG = new ValidationError(ILLEGAL_VALUE, "username",
            "The username is too long.");


    public static final ValidationError MISSING_E_MAIL = new ValidationError(MISSING_VALUE, "email",
            "The email is missing.");
    public static final ValidationError E_MAIL_TOO_SHORT = new ValidationError(ILLEGAL_VALUE, "email",
            "The email is too short.");
    public static final ValidationError E_MAIL_TOO_LONG = new ValidationError(ILLEGAL_VALUE, "email",
            "The email is too long.");
    public static final ValidationError INVALID_E_MAIL = new ValidationError(ILLEGAL_VALUE, "email",
            "The email is not valid.");


    public static final ValidationError MISSING_PASSWORD = new ValidationError(MISSING_VALUE, "password",
            "The password is missing.");
    public static final ValidationError PASSWORD_TOO_SHORT = new ValidationError(ILLEGAL_VALUE, "password",
            "The password is too short.");
    public static final ValidationError PASSWORD_TOO_LONG = new ValidationError(ILLEGAL_VALUE, "password",
            "The password is too long.");


    public static final ValidationError MISSING_USER = new ValidationError(MISSING_VALUE, "user",
            "The user is missing.");

    public static final ValidationError MISSING_ROLE = new ValidationError(MISSING_VALUE, "role",
            "The role is missing.");


    public static final ValidationError MISSING_TEMPERATURE = new ValidationError(MISSING_VALUE, "temperature",
            "The temperature is missing.");

    public static final ValidationError TOO_LOW_TEMPERATURE = new ValidationError(MISSING_VALUE, "temperature",
            "The temperature is too low.");

    public static final ValidationError TOO_HIGH_TEMPERATURE = new ValidationError(MISSING_VALUE, "temperature",
            "The temperature is too high.");


    public static final ValidationError NICKNAME_TOO_SHORT = new ValidationError(MISSING_VALUE, "nickname",
            "The nickname is too short.");

    public static final ValidationError NICKNAME_TOO_LONG = new ValidationError(MISSING_VALUE, "nickname",
            "The nickname is too long.");


    public static final ValidationError MISSING_DEVICE = new ValidationError(MISSING_VALUE, "device",
            "The device is missing.");

    public static final ValidationError MISSING_OWNER = new ValidationError(MISSING_VALUE, "owner",
            "The owner is missing.");

    public static final ValidationError MISSING_PREFERENCE_NAME = new ValidationError(MISSING_VALUE, "name",
            "The name is missing.");
    public static final ValidationError PREFERENCE_NAME_TOO_SHORT = new ValidationError(ILLEGAL_VALUE, "name",
            "The name is too short.");
    public static final ValidationError PREFERENCE_NAME_TOO_LONG = new ValidationError(ILLEGAL_VALUE, "name",
            "The name is too long.");
}
