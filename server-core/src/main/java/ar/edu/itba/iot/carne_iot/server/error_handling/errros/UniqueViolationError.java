package ar.edu.itba.iot.carne_iot.server.error_handling.errros;

/**
 * Class representing an error that occurs when setting an already in use value (or group of values)
 * that must be unique.
 */
public final class UniqueViolationError extends MultiFieldError {

    /**
     * Constructor.
     *
     * @param message      A helper message to be used for debugging purposes.
     * @param uniqueFields The group of values that must be unique.
     */
    public UniqueViolationError(String message, String... uniqueFields) {
        super(message, uniqueFields);
    }
}
