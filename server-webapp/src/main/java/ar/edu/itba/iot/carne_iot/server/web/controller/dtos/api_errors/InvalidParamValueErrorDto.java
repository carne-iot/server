package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for client errors caused when setting an invalid value to a param (path or query).
 */
public class InvalidParamValueErrorDto extends ClientErrorDto {

    /**
     * The parameters whose value is invalid.
     */
    @JsonProperty
    private final String conflictingParam; // Jersey only checks one parameter

    /**
     * Constructor.
     *
     * @param conflictingParam The parameters whose value is invalid.
     */
    public InvalidParamValueErrorDto(String conflictingParam) {
        super(ErrorFamily.INVALID_PARAM_VALUE);
        this.conflictingParam = conflictingParam;
    }

    /**
     * @return The parameters whose value is invalid.
     */
    public String getConflictingParam() {
        return conflictingParam;
    }
}
