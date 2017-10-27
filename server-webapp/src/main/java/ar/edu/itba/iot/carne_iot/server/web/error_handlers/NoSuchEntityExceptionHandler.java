package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.exceptions.NoSuchEntityException;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link NoSuchEntityException}.
 * Will result into a <b>404 Not Found</b> response.
 */
@ExceptionHandlerObject
/* package */ class NoSuchEntityExceptionHandler implements ExceptionHandler<NoSuchEntityException> {

    @Override
    public ErrorHandler.HandlingResult handle(NoSuchEntityException exception) {
        return new ErrorHandler.HandlingResult(Response.Status.NOT_FOUND.getStatusCode(), null);
    }
}
