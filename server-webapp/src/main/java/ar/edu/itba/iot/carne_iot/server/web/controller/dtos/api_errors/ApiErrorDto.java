package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for errors returned by the API.
 */
public abstract class ApiErrorDto {

    /**
     * The {@link ErrorKind} (i.e Client or Server error).
     */
    @JsonProperty
    private final ErrorKind errorKind;

    /**
     * A human-readable description of the error kind.
     */
    @JsonProperty
    private final String errorKindDescription;

    /**
     * A human readable message for the person consuming the API.
     */
    @JsonProperty
    private final String message;

    /**
     * Constructor.
     *
     * @param errorKind The {@link ErrorKind} (i.e Client or Server error).
     * @param message   A human readable message for the person consuming the API.
     */
    /* package */ ApiErrorDto(ErrorKind errorKind, String message) {
        this.errorKind = errorKind;
        this.errorKindDescription = errorKind.getDescription();
        this.message = message;
    }

    /**
     * @return The {@link ErrorKind} (i.e Client or Server error).
     */
    public ErrorKind getErrorKind() {
        return errorKind;
    }

    /**
     * @return A human-readable description of the error kind.
     */
    public String getErrorKindDescription() {
        return errorKindDescription;
    }

    /**
     * @return A human readable message for the person consuming the API.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Enum containing kind of errors the system can reach.
     */
    public enum ErrorKind {
        CLIENT {
            @Override
            public String getDescription() {
                return "Client side error";
            }
        },
        SERVER {
            @Override
            protected String getDescription() {
                return "Server side error";
            }
        };

        /**
         * @return A human-readable description of the error kind.
         */
        protected abstract String getDescription();
    }
}
