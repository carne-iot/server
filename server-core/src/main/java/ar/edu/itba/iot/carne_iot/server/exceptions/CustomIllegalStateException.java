package ar.edu.itba.iot.carne_iot.server.exceptions;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.IllegalStateError;

/**
 * {@link RuntimeException} thrown when trying to perform an operation over an entity
 * when the state of it is invalid for that operation.
 */
public class CustomIllegalStateException extends RuntimeException {

    /**
     * The {@link IllegalStateError} that caused this exception to be thrown.
     */
    private final IllegalStateError error;

    /**
     * Default constructor.
     *
     * @param error The {@link IllegalStateError} that caused this exception to be thrown.
     */
    public CustomIllegalStateException(IllegalStateError error) {
        super();
        this.error = error;
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param error   The {@link IllegalStateError} that caused this exception to be thrown.
     */
    public CustomIllegalStateException(IllegalStateError error, String message) {
        super(message);
        this.error = error;
    }

    /**
     * Constructor which can set a {@code message} and a {@code cause}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     * @param error   The {@link IllegalStateError} that caused this exception to be thrown.
     */
    public CustomIllegalStateException(IllegalStateError error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    /**
     * @return The {@link IllegalStateError} that caused this exception to be thrown.
     */
    public IllegalStateError getError() {
        return error;
    }
}
