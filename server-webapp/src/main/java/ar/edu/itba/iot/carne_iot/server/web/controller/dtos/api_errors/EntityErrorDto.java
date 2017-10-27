package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.EntityError;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data transfer object for client errors caused by trying to create or update and entity in a wrong way.
 */
public abstract class EntityErrorDto<T extends EntityError> extends ClientErrorDto {

    /**
     * The {@link List} of {@link EntityError}s.
     */
    @JsonProperty
    private final List<T> errors;

    /**
     * Constructor.
     *
     * @param errorFamily The {@link ErrorFamily}.
     * @param errors      The {@link List} of {@link EntityError}s.
     */
    /* package */ EntityErrorDto(ErrorFamily errorFamily, List<T> errors) {
        super(errorFamily);
        this.errors = errors;
    }

    /**
     * @return The {@link List} of {@link EntityError}s.
     */
    public List<T> getErrors() {
        return errors;
    }
}
