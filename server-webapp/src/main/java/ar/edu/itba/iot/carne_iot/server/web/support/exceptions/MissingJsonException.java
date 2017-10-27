package ar.edu.itba.iot.carne_iot.server.web.support.exceptions;

/**
 * A {@link RuntimeException} that must be thrown when a JSON is expected, but the API consumer did not send any.
 */
public class MissingJsonException extends RuntimeException {

    /**
     * Default constructor.
     */
    public MissingJsonException() {
        super();
    }

    /**
     * Constructor which can set a {@code message}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public MissingJsonException(String message) {
        super(message);
    }

    /**
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
     */
    public MissingJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
