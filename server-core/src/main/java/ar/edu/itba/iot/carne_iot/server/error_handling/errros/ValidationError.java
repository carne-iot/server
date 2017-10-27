package ar.edu.itba.iot.carne_iot.server.error_handling.errros;

/**
 * Class representing an error that occurs when validating an entity.
 * There are two types of validation errors: a mandatory value is missing, or a value is not a legal one
 * (see {@link ErrorCause} for more information).
 */
public final class ValidationError extends EntityError {

    /**
     * The {@link ErrorCause} representing what caused the new error.
     */
    private final ErrorCause cause;

    /**
     * The fields that did not validate.
     */
    private final String field;


    /**
     * Constructor.
     *
     * @param cause   The {@link ErrorCause} representing what caused the new error.
     * @param field   The field that did not validate.
     * @param message A helper message to be used for debugging purposes.
     */
    public ValidationError(ErrorCause cause, String field, String message) {
        super(message);
        this.cause = cause;
        this.field = field;
    }


    /**
     * @return The {@link ErrorCause} representing what caused the new error.
     */
    public ErrorCause getCause() {
        return cause;
    }

    /**
     * @return The fields that did not validate.
     */
    public String getField() {
        return field;
    }


    /**
     * En enum indicating types of validation errors.
     */
    public enum ErrorCause {
        /**
         * Error that occurs when a mandatory value is missing (i.e was set to {@code null}).
         */
        MISSING_VALUE,
        /**
         * Error that occurs when a value does not match the constraints established for it.
         */
        ILLEGAL_VALUE,
    }
}
