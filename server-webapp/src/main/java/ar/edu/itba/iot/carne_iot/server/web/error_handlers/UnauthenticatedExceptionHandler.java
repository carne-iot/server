package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.exceptions.UnauthenticatedException;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link UnauthenticatedException}.
 * Will result into a <b>401 Unauthorized</b> response.
 */
@ExceptionHandlerObject
/* package */ class UnauthenticatedExceptionHandler implements ExceptionHandler<UnauthenticatedException> {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthenticatedExceptionHandler.class);

    @Override
    public ErrorHandler.HandlingResult handle(UnauthenticatedException exception) {
        LOGGER.debug("A user was not authenticated. UnauthenticatedException message: {}", exception.getMessage());
        LOGGER.trace("UnauthenticatedException Stack trace: ", exception);

        return unauthenticatedResult();
    }

    /**
     * Static method that creates a {@link ErrorHandler.HandlingResult} with <b>401 Unauthorized</b>
     * response status code, and empty body.
     * Can be reused by other {@link ExceptionHandler}s that must return the same results.
     *
     * @return The {@link ErrorHandler.HandlingResult}.
     */
    /* package */
    static ErrorHandler.HandlingResult unauthenticatedResult() {
        return new ErrorHandler.HandlingResult(Response.Status.UNAUTHORIZED.getStatusCode(), null);
    }
}
