package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;

import javax.ws.rs.WebApplicationException;

/**
 * {@link ExceptionHandler} in charge of handling {@link WebApplicationException}.
 * Will result into the result code obtained from {@link WebApplicationException#getResponse()}, with no entity.
 */
@ExceptionHandlerObject
/* package */ class WebApplicationExceptionHandler implements ExceptionHandler<WebApplicationException> {

    @Override
    public ErrorHandler.HandlingResult handle(WebApplicationException exception) {
        return new ErrorHandler.HandlingResult(exception.getResponse().getStatus(), null);
    }
}
