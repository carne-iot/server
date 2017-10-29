package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.exceptions.CustomIllegalStateException;
import ar.edu.itba.iot.carne_iot.server.exceptions.UniqueViolationException;
import ar.edu.itba.iot.carne_iot.server.web.Constants;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.IllegalStateErrorDto;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;

/**
 * {@link ExceptionHandler} in charge of handling {@link UniqueViolationException}.
 * Will result into a <b>409 Conflict</b> response.
 */
@ExceptionHandlerObject
/* package */ class CustomIllegalStateExceptionHandler implements ExceptionHandler<CustomIllegalStateException> {

    @Override
    public ErrorHandler.HandlingResult handle(CustomIllegalStateException exception) {
        return new ErrorHandler.HandlingResult(Constants.MissingHttpStatuses.UNPROCESSABLE_ENTITY.getStatusCode(),
                new IllegalStateErrorDto(exception.getError()));
    }
}
