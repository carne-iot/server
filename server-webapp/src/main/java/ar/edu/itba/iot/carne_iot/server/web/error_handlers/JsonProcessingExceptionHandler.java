package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.RepresentationErrorDto;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link JsonProcessingException}.
 * Will result into a <b>400 Bad Request</b> response.
 */
@ExceptionHandlerObject
/* package */ class JsonProcessingExceptionHandler implements ExceptionHandler<JsonProcessingException> {

    @Override
    public ErrorHandler.HandlingResult handle(JsonProcessingException exception) {
        return jsonProcessingExceptionHandlingResult();
    }

    /**
     * Static method that creates a {@link ErrorHandler.HandlingResult} with <b>400 Bad Request</b> response status code,
     * including the {@link RepresentationErrorDto#REPRESENTATION_ERROR_DTO} as the entity.
     * Can be reused by other {@link ExceptionHandler}s that must return the same results.
     *
     * @return The {@link ErrorHandler.HandlingResult}.
     */
    /* package */
    static ErrorHandler.HandlingResult jsonProcessingExceptionHandlingResult() {
        return new ErrorHandler.HandlingResult(Response.Status.BAD_REQUEST.getStatusCode(),
                RepresentationErrorDto.REPRESENTATION_ERROR_DTO);
    }
}
