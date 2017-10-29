package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for API errors that are caused by the client consuming it.
 */
public abstract class ClientErrorDto extends ApiErrorDto {

    /**
     * The {@link ClientErrorDto.ErrorFamily}.
     */
    @JsonProperty
    private final ErrorFamily errorFamily;

    /**
     * A human-readable description of the client error family.
     */
    @JsonProperty
    private final String errorFamilyDescription;


    /**
     * Constructor.
     *
     * @param errorFamily The {@link ClientErrorDto.ErrorFamily}.
     */
    /* package */ ClientErrorDto(ErrorFamily errorFamily) {
        super(ErrorKind.CLIENT, errorFamily.getMessage());
        this.errorFamily = errorFamily;
        this.errorFamilyDescription = errorFamily.getDescription();
    }

    /**
     * @return The {@link ClientErrorDto.ErrorFamily}.
     */
    public ErrorFamily getErrorFamily() {
        return errorFamily;
    }

    /**
     * @return A human-readable description of the client error family.
     */
    public String getErrorFamilyDescription() {
        return errorFamilyDescription;
    }

    /**
     * Enum containing the family of client errors.
     */
    public enum ErrorFamily {
        /**
         * Indicates that a given parameter (path, query, etc.) value is not valid
         * (e.g sending alphabetic characters when an integer is expected).
         */
        INVALID_PARAM_VALUE {
            @Override
            protected String getDescription() {
                return "Invalid Parameter Value";
            }

            @Override
            protected String getMessage() {
                return "There were invalid parameter (path or query) errors";
            }
        },
        /**
         * Indicates that a given parameter (path, query, etc.) value is not legal
         * (e.g sending a negative number when a positive integer is expected).
         */
        ILLEGAL_PARAM_VALUE {
            @Override
            protected String getDescription() {
                return "Illegal Parameter Value";
            }

            @Override
            protected String getMessage() {
                return "There were illegal parameter (path or query) errors";
            }
        },
        /**
         * Indicates that a sent entity can't be interpreted as it should be.
         * Examples of this errors are malformed JSONs (e.g a JSON missing double quotes for field names),
         * missing required properties, unknown properties were included,
         * or invalid formats (e.g sending a string when a number is expected).
         */
        REPRESENTATION {
            @Override
            protected String getDescription() {
                return "Entity Representation Error";
            }

            @Override
            protected String getMessage() {
                return "There were entity representation errors";
            }
        },
        /**
         * Indicates that a referenced entity does not exists (i.e similar to a foreign key constraint violation).
         */
        NOT_PRESENT_REFERENCE {
            @Override
            protected String getDescription() {
                return "Entities Not Present Error";
            }

            @Override
            protected String getMessage() {
                return "There were not present entities references errors";
            }
        },
        /**
         * Indicates that a value (or group of values) that must be unique, and already exist, was sent in an entity
         * (e.g if the email must be unique for each user, this error is thrown when sending an already used email).
         */
        UNIQUE_VIOLATION {
            @Override
            protected String getDescription() {
                return "Unique Violation Error";
            }

            @Override
            protected String getMessage() {
                return "There were unique value (or group of values) violation errors";
            }
        },
        /**
         * Indicates that there were validation problems when creating or updating an entity.
         * They can be missing values for mandatory fields (e.g username for users),
         * or illegal values (e.g future dates for a birth date).
         */
        VALIDATION {
            @Override
            protected String getDescription() {
                return "Validation Errors";
            }

            @Override
            protected String getMessage() {
                return "There were validation errors when creating or updating an entity";
            }
        },
        /**
         * Indicates that an operation over an entity was tried to be performed when the state of it was invalid
         * for the operation to be performed).
         */
        ILLEGAL_STATE {
            @Override
            protected String getDescription() {
                return "Illegal State Error";
            }

            @Override
            protected String getMessage() {
                return "An operation was tried to be performed over an entity when its state " +
                        "was not valid for the operation to be performed ";
            }
        };

        /**
         * @return A human-readable description of the client error family.
         */
        protected abstract String getDescription();

        /**
         * @return A human readable message for the person consuming the API.
         */
        protected abstract String getMessage();
    }
}
