package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

import java.util.List;

/**
 * Data transfer object for client errors caused when setting an illegal value to a param (path or query).
 */
public class IllegalParamValueErrorDto extends ClientErrorDto {

    /**
     * A {@link List} containing those parameters with illegal values.
     */
    private final List<String> conflictingParams;

    /**
     * Constructor.
     *
     * @param conflictingParams A {@link List} containing those parameters with illegal values.
     */
    public IllegalParamValueErrorDto(List<String> conflictingParams) {
        super(ErrorFamily.ILLEGAL_PARAM_VALUE);
        this.conflictingParams = conflictingParams;
    }

    /**
     * @return A {@link List} containing those parameters with illegal values.
     */
    public List<String> getConflictingParams() {
        return conflictingParams;
    }
}
