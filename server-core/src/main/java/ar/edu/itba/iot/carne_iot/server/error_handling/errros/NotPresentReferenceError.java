package ar.edu.itba.iot.carne_iot.server.error_handling.errros;

/**
 * Class representing an error that occurs when referencing an entity that does not exist.
 * (This reference can be through one or more values).
 */
public final class NotPresentReferenceError extends MultiFieldError {

    /**
     * Constructor.
     *
     * @param message     A human-readable message to be used for debugging purposes.
     * @param errorFields The group of fields that altogether trigger the error.
     */
    public NotPresentReferenceError(String message, String[] errorFields) {
        super(message, errorFields);
    }
}
