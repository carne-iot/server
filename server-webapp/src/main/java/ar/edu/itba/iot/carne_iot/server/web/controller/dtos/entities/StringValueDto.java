package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data transfer object for receiving only one value from an API consumer
 */
public class StringValueDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String value;

    public StringValueDto() {
        // For Jersey
    }

    public String getValue() {
        return value;
    }

}
