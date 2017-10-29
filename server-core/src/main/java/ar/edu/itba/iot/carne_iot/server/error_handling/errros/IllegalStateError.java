package ar.edu.itba.iot.carne_iot.server.error_handling.errros;

/**
 * Class representing an error that occurs when operating over an entity in an invalid state
 * (to perform that operation).
 */
public final class IllegalStateError extends EntityError {

    /**
     * The entity that is in an invalid state.
     */
    private final String invalidStateEntity;

    /**
     * Constructor.
     *
     * @param message            A helper message to be used for debugging purposes.
     * @param invalidStateEntity The entity that is in an invalid state.
     */
    public IllegalStateError(String message, String invalidStateEntity) {
        super(message);
        this.invalidStateEntity = invalidStateEntity;
    }

    /**
     * @return The entity that is in an invalid state.
     */
    public String getInvalidStateEntity() {
        return invalidStateEntity;
    }
}
