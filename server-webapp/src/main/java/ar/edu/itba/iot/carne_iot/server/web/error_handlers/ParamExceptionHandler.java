package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.InvalidParamValueErrorDto;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import org.glassfish.jersey.server.ParamException;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link ParamException}.
 * Will result into a <b>400 Bad Request</b> response.
 */
@ExceptionHandlerObject
/* package */ class ParamExceptionHandler implements ExceptionHandler<ParamException> {

    @Override
    public ErrorHandler.HandlingResult handle(ParamException exception) {
        return new ErrorHandler.HandlingResult(Response.Status.BAD_REQUEST.getStatusCode(),
                new InvalidParamValueErrorDto(exception.getParameterName()));
    }
}
