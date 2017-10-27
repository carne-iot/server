package ar.edu.itba.iot.carne_iot.server.exceptions;

/**
 * {@link RuntimeException} thrown when searching an entity that does not exist.
 */
public class NoSuchEntityException extends RuntimeException {

    /**
     * Default constructor.
     */
    public NoSuchEntityException() {
        super();
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public NoSuchEntityException(String message) {
        super(message);
    }

    /**
     * Constructor which can set a mes{@code message} and a {@code cause}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     */
    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
