package ar.edu.itba.iot.carne_iot.server.error_handling.errros;

/**
 * Class representing an error that occurs when trying to create or update an entity wrongly
 * (i.e unique violations, not present references or validations).
 */
public abstract class EntityError {

    /**
     * A human-readable message to be used for debugging purposes.
     */
    private final String message;

    /**
     * Constructor.
     *
     * @param message A human-readable message to be used for debugging purposes.
     */
    /* package */ EntityError(String message) {
        this.message = message;
    }

    /**
     * @return A human-readable message to be used for debugging purposes.
     */
    public String getMessage() {
        return message;
    }
}
