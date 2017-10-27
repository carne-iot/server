package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.exceptions.UnauthorizedException;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link UnauthorizedException}.
 * Will result into a <b>403 Forbidden</b> response.
 */
@ExceptionHandlerObject
/* package */ class UnauthorizedExceptionHandler implements ExceptionHandler<UnauthorizedException> {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedExceptionHandler.class);

    @Override
    public ErrorHandler.HandlingResult handle(UnauthorizedException exception) {
        LOGGER.debug("A user was not authorized. UnauthorizedException message: {}", exception.getMessage());
        LOGGER.trace("UnauthorizedException Stack trace: ", exception);

        return unauthorizedResult();
    }

    /**
     * Static method that creates a {@link ErrorHandler.HandlingResult} with <b>403 Forbidden</b> response status code,
     * and empty body.
     * Can be reused by other {@link ExceptionHandler}s that must return the same results.
     *
     * @return The {@link ErrorHandler.HandlingResult}.
     */
    /* package */
    static ErrorHandler.HandlingResult unauthorizedResult() {
        return new ErrorHandler.HandlingResult(Response.Status.FORBIDDEN.getStatusCode(), null);
    }
}
