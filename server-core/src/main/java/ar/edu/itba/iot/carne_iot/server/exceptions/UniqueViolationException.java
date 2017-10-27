package ar.edu.itba.iot.carne_iot.server.exceptions;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.UniqueViolationError;

import java.util.List;

/**
 * {@link RuntimeException} thrown when trying to use an already in use value (or group of values) that must be unique.
 */
public class UniqueViolationException extends RuntimeException {

    /**
     * A {@link List} containing all {@link UniqueViolationError}s that caused this exception to be thrown.
     */
    private final List<UniqueViolationError> errorList;

    /**
     * Default constructor.
     *
     * @param errorList A {@link List} containing all {@link UniqueViolationError}s that caused the exception to be thrown.
     */
    public UniqueViolationException(List<UniqueViolationError> errorList) {
        super();
        this.errorList = errorList;
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message   The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param errorList A {@link List} containing all {@link UniqueViolationError}s that caused the exception to be thrown.
     */
    public UniqueViolationException(String message, List<UniqueViolationError> errorList) {
        super(message);
        this.errorList = errorList;
    }

    /**
     * Constructor which can set a {@code message} and a {@code cause}.
     *
     * @param message   The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause     The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                  For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     * @param errorList A {@link List} containing all {@link UniqueViolationError}s that caused the exception to be thrown.
     */
    public UniqueViolationException(String message, Throwable cause, List<UniqueViolationError> errorList) {
        super(message, cause);
        this.errorList = errorList;
    }

    /**
     * @return The {@link List} of {@link UniqueViolationError}s that caused this exception to be thrown.
     */
    public List<UniqueViolationError> getErrors() {
        return errorList;
    }
}
