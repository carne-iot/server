package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.ServerErrorDto;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link Throwable}.
 * Will result into a <b>500 Internal Server Error</b> response.
 */
@ExceptionHandlerObject
/* package */ class ThrowableHandler implements ExceptionHandler<Throwable> {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThrowableHandler.class);

    @Override
    public ErrorHandler.HandlingResult handle(Throwable exception) {
        LOGGER.error("An uncaught exception was thrown", exception);

        return new ErrorHandler.HandlingResult(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                ServerErrorDto.BASIC_SERVER_ERROR_DTO);
    }
}
