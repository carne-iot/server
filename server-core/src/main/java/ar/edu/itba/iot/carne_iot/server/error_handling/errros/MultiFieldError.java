package ar.edu.itba.iot.carne_iot.server.error_handling.errros;

/**
 * Class representing an error that occurs when a client interacts wrongly with the system,
 * being a group of fields those that (altogether) provoke the error.
 */
public abstract class MultiFieldError extends EntityError {

    /**
     * The group of fields that altogether trigger the error.
     */
    private final String[] errorFields;

    /**
     * Constructor.
     *
     * @param message     A human-readable message to be used for debugging purposes.
     * @param errorFields The group of fields that altogether trigger the error.
     */
    /* package */ MultiFieldError(String message, String[] errorFields) {
        super(message);
        this.errorFields = errorFields;
    }

    /**
     * @return The group of fields that altogether trigger the error.
     */
    public String[] getErrorFields() {
        return errorFields;
    }
}
