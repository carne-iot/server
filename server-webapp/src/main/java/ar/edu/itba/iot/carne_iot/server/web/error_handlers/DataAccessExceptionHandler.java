package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.ServerErrorDto;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.ws.rs.core.Response;

/**
 * {@link ExceptionHandler} in charge of handling {@link DataAccessException}.
 * Will result into a <b>503 Service Unavailable</b> response.
 */
@ExceptionHandlerObject
/* package */ class DataAccessExceptionHandler implements ExceptionHandler<DataAccessException> {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataAccessExceptionHandler.class);

    @Override
    public ErrorHandler.HandlingResult handle(DataAccessException exception) {
        LOGGER.error("Could not access database", exception);

        return new ErrorHandler.HandlingResult(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(),
                ServerErrorDto.DATABASE_ACCESS_ERROR_DTO);
    }
}
