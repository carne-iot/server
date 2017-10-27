package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.IllegalParamValueErrorDto;
import ar.edu.itba.iot.carne_iot.server.web.support.exceptions.IllegalParamValueException;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link IllegalParamValueException}.
 * Will result into a <b>400 Bad Request</b> response.
 */
@ExceptionHandlerObject
/* package */ class IllegalParamValueExceptionHandler implements ExceptionHandler<IllegalParamValueException> {

    @Override
    public ErrorHandler.HandlingResult handle(IllegalParamValueException exception) {
        return illegalParamValueHandlingResult(new IllegalParamValueErrorDto(exception.getConflictingParams()));
    }

    /**
     * Static method that creates a {@link ErrorHandler.HandlingResult} with <b>400 Bad Request</b> response status code,
     * including the given {@link IllegalParamValueErrorDto} as the entity.
     * Can be reused by other {@link ExceptionHandler}s that must return the same results.
     *
     * @return The {@link ErrorHandler.HandlingResult}.
     */
    /* package */
    static ErrorHandler.HandlingResult illegalParamValueHandlingResult(IllegalParamValueErrorDto illegalParamValueErrorDto) {
        return new ErrorHandler.HandlingResult(Response.Status.BAD_REQUEST.getStatusCode(), illegalParamValueErrorDto);
    }
}
