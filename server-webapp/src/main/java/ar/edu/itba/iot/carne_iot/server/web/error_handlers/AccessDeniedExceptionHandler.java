package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

/**
 * {@link ExceptionHandler} in charge of handling {@link AccessDeniedException}.
 * Will result in the return value of {@link UnauthorizedExceptionHandler#unauthorizedResult()}.
 */
@ExceptionHandlerObject
/* package */ class AccessDeniedExceptionHandler implements ExceptionHandler<AccessDeniedException> {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedExceptionHandler.class);

    @Override
    public ErrorHandler.HandlingResult handle(AccessDeniedException exception) {
        LOGGER.debug("A user was not allowed to execute a method by Spring security method security mechanism. " +
                "AccessDeniedException message: {}", exception.getMessage());
        LOGGER.trace("AccessDeniedException Stack trace: ", exception);

        return UnauthorizedExceptionHandler.unauthorizedResult();
    }
}
