package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.UniqueViolationError;

import java.util.List;

/**
 * Data transfer object for client errors caused by trying to set a value that is already used and must be unique.
 */
public final class UniqueViolationErrorDto extends EntityErrorDto<UniqueViolationError> {

    /**
     * @param errors The {@link List} of {@link UniqueViolationError}s.
     */
    public UniqueViolationErrorDto(List<UniqueViolationError> errors) {
        super(ErrorFamily.UNIQUE_VIOLATION, errors);
    }
}
