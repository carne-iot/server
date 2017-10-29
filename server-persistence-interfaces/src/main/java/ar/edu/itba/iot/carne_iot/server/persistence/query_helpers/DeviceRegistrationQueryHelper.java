package ar.edu.itba.iot.carne_iot.server.persistence.query_helpers;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration;
import ar.edu.itba.iot.carne_iot.server.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Defines behaviour of an object in charge of helping the task of querying {@link DeviceRegistration}s
 * by a {@link ar.edu.itba.iot.carne_iot.server.persistence.daos.DeviceRegistrationDao}.
 */
public interface DeviceRegistrationQueryHelper {

    /**
     * Creates a new {@link Specification} of {@link DeviceRegistration}
     * used to query them according to the given parameters,applying ANDs between them.
     *
     * @param owner A filter for the {@link DeviceRegistration}s that are applied to a given {@link User}.
     * @return The {@link Specification} of {@link DeviceRegistration}
     * that can be used to get those {@link DeviceRegistration}s matching the give parameters.
     * @throws IllegalArgumentException if the {@code owner} is {@code null}.
     * @apiNote Those parameter that are {@code null} must not be taken into account in the {@link Specification}.
     */
    Specification<DeviceRegistration> createDeviceSpecification(User owner) throws IllegalArgumentException;

    /**
     * Validates that the given {@link Pageable} is valid for querying {@link DeviceRegistration}s.
     *
     * @param pageable The {@link Pageable} to be validated.
     * @throws InvalidPropertiesException If it has a {@link org.springframework.data.domain.Sort}
     *                                    with invalid properties.
     */
    void validatePageable(Pageable pageable) throws InvalidPropertiesException;

    /**
     * Changes the given {@link Pageable} in order to apply sorting by
     * {@link ar.edu.itba.iot.carne_iot.server.models.Device} properties.
     *
     * @param sort The {@link Pageable} to be adapted.
     */
    Sort adaptSort(Sort sort);
}
