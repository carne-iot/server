package ar.edu.itba.iot.carne_iot.server.web.config;

import com.bellotapps.utils.error_handler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

/**
 * The global {@link ExceptionMapper} that is in charge of mapping any {@link Throwable} thrown
 * within the Jersey application, using an {@link ErrorHandler}.
 */
@Provider
@Component
/* package */ class ThrowableMapper implements ExceptionMapper<Throwable> {

    /**
     * The {@link ErrorHandler} in charge of transforming an exception into data to be returned in the response.
     */
    private final ErrorHandler errorHandler;

    @Autowired
    /* package */ ThrowableMapper(ErrorHandler exceptionHandler) {
        this.errorHandler = exceptionHandler;
    }

    @Override
    public Response toResponse(Throwable exception) {
        final ErrorHandler.HandlingResult result = errorHandler.handle(exception);
        return Response.status(result.getHttpErrorCode())
                .entity(Optional.ofNullable(result.getErrorRepresentationEntity()).orElse(""))
                .build();
    }
}
