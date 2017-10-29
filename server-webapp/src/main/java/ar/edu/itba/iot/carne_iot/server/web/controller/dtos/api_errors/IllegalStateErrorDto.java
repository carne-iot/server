package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.IllegalStateError;
import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;

/**
 * Data transfer object for client errors caused when an operation over an entity is not valid  because of its state.
 */
public final class IllegalStateErrorDto extends ClientErrorDto {

    /**
     * The {@link ValidationError}.
     */
    private final IllegalStateError error;

    /**
     * Constructor.
     *
     * @param error The {@link ValidationError}.
     */
    public IllegalStateErrorDto(IllegalStateError error) {
        super(ErrorFamily.ILLEGAL_STATE);
        this.error = error;
    }

    /**
     * @return The {@link ValidationError}.
     */
    public IllegalStateError getError() {
        return error;
    }
}
