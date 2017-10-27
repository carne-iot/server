package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;
import ar.edu.itba.iot.carne_iot.server.web.Constants;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.ValidationErrorDto;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;

/**
 * {@link ExceptionHandler} in charge of handling {@link ValidationException}.
 * Will result into a <b>422 Unprocessable Entity</b> response.
 */
@ExceptionHandlerObject
/* package */ class ValidationViolationExceptionHandler implements ExceptionHandler<ValidationException> {

    @Override
    public ErrorHandler.HandlingResult handle(ValidationException exception) {
        return new ErrorHandler.HandlingResult(Constants.MissingHttpStatuses.UNPROCESSABLE_ENTITY.getStatusCode(),
                new ValidationErrorDto(exception.getErrors()));
    }
}
