package ar.edu.itba.iot.carne_iot.server.web.error_handlers;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.api_errors.IllegalParamValueErrorDto;
import com.bellotapps.utils.error_handler.ErrorHandler;
import com.bellotapps.utils.error_handler.ExceptionHandler;
import com.bellotapps.utils.error_handler.ExceptionHandlerObject;

/**
 * {@link ExceptionHandler} in charge of handling {@link InvalidPropertiesException}.
 * Will result in the return value of
 * {@link IllegalParamValueExceptionHandler#illegalParamValueHandlingResult(IllegalParamValueErrorDto)}.
 */
@ExceptionHandlerObject
/* package */ class InvalidPropertyExceptionHandler implements ExceptionHandler<InvalidPropertiesException> {

    @Override
    public ErrorHandler.HandlingResult handle(InvalidPropertiesException exception) {
        return IllegalParamValueExceptionHandler
                .illegalParamValueHandlingResult(new IllegalParamValueErrorDto(exception.getErrors()));
    }
}
