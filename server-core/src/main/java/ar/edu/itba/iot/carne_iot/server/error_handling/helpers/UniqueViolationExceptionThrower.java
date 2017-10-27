package ar.edu.itba.iot.carne_iot.server.error_handling.helpers;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.UniqueViolationError;
import ar.edu.itba.iot.carne_iot.server.exceptions.UniqueViolationException;

import java.util.List;
import java.util.Objects;

/**
 * An interface implementing a default method used to throw a {@link UniqueViolationException}.
 * This allows reusing the method in an aspect oriented way.
 */
public interface UniqueViolationExceptionThrower {

    /**
     * Throws a {@link UniqueViolationException} if the given {@code errorList} is not empty.
     *
     * @param errorList A {@link List} that might contain {@link UniqueViolationError}s
     *                  that were detected while validating changes over an entity.
     */
    default void throwUniqueViolationException(List<UniqueViolationError> errorList) {
        Objects.requireNonNull(errorList, "The error list must not be null");
        if (!errorList.isEmpty()) {
            throw new UniqueViolationException(errorList);
        }
    }

}
