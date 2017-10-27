package ar.edu.itba.iot.carne_iot.server.web.security.authentication;

import ar.edu.itba.iot.carne_iot.server.exceptions.UnauthenticatedException;

/**
 * Exception thrown when there are JWT issues
 * (e.g invalid token, expired token, wrong or missing signature, blacklisted, etc).
 */
/* package */ class JwtException extends UnauthenticatedException {

    /**
     * Default constructor.
     */
    /* package */ JwtException() {
        super();
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    /* package */ JwtException(String message) {
        super(message);
    }

    /**
     * Constructor which can set a mes{@code message} and a {@code cause}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     */
    /* package */ JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
