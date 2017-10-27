package ar.edu.itba.iot.carne_iot.server.exceptions;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;

import java.util.List;

/**
 * A {@link RuntimeException} that represents an entity validation error.
 */
public class ValidationException extends RuntimeException {

    /**
     * A {@link List} containing all {@link ValidationError}s that caused this exception to be thrown.
     */
    private final List<ValidationError> errorList;

    /**
     * Default constructor.
     *
     * @param errorList A {@link List} containing all {@link ValidationError}s that caused the exception to be thrown.
     */
    public ValidationException(List<ValidationError> errorList) {
        super();
        this.errorList = errorList;
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message   The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param errorList A {@link List} containing all {@link ValidationError}s that caused the exception to be thrown.
     */
    public ValidationException(String message, List<ValidationError> errorList) {
        super(message);
        this.errorList = errorList;
    }

    /**
     * Constructor which can set a {@code message} and a {@code cause}.
     *
     * @param message   The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause     The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                  For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     * @param errorList A {@link List} containing all {@link ValidationError}s that caused the exception to be thrown.
     */
    public ValidationException(String message, Throwable cause, List<ValidationError> errorList) {
        super(message, cause);
        this.errorList = errorList;
    }

    /**
     * @return The {@link List} of {@link ValidationError}s that caused this exception to be thrown.
     */
    public List<ValidationError> getErrors() {
        return errorList;
    }
}
