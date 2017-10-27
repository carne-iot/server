package ar.edu.itba.iot.carne_iot.server.security;

import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;

/**
 * Defines behaviour of an object that is in charge of validating a password.
 */
public interface PasswordValidator {

    /**
     * Checks whether the given {@code password} is valid, throwing a {@link ValidationException} if not.
     *
     * @param password The password to be validated.
     * @throws ValidationException If the given {@code password} is not valid.
     */
    void validate(CharSequence password) throws ValidationException;
}
