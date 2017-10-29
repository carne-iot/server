package ar.edu.itba.iot.carne_iot.server.persistence.query_helpers;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration;
import org.springframework.data.domain.Pageable;

/**
 * Defines behaviour of an object in charge of helping the task of querying
 * {@link ar.edu.itba.iot.carne_iot.server.models.Device}s
 * by a {@link ar.edu.itba.iot.carne_iot.server.persistence.daos.DeviceDao}.
 */
public interface DeviceQueryHelper {

    /**
     * Validates that the given {@link Pageable} is valid for querying {@link DeviceRegistration}s.
     *
     * @param pageable The {@link Pageable} to be validated.
     * @throws InvalidPropertiesException If it has a {@link org.springframework.data.domain.Sort}
     *                                    with invalid properties.
     */
    void validatePageable(Pageable pageable) throws InvalidPropertiesException;
}
