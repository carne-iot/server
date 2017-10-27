package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors;

/**
 * Data transfer object for client errors caused by a bad representation of an entity.
 */
public final class RepresentationErrorDto extends ClientErrorDto {

    /**
     * The {@link RepresentationErrorDto} to be sent when there are representation errors.
     * Using this constant avoids creating one each time there are problems.
     */
    public static final RepresentationErrorDto REPRESENTATION_ERROR_DTO = new RepresentationErrorDto();

    /**
     * Constructor.
     */
    private RepresentationErrorDto() {
        super(ErrorFamily.REPRESENTATION);
    }

}
