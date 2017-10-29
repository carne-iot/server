package ar.edu.itba.iot.carne_iot.server.persistence.daos;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.custom_repositories.ExtendedJpaRepository;

import java.util.Optional;

/**
 * Defines behaviour of the DAO in charge of managing {@link DeviceRegistration}s data.
 */
public interface DeviceRegistrationDao extends ExtendedJpaRepository<DeviceRegistration, Long> {

    /**
     * Retrieves the active {@link DeviceRegistration} with the given {@link Device}.
     *
     * @param device The registered {@link Device}.
     * @return A <b>nullable</b> {@link Optional} of {@link DeviceRegistration}
     * containing the {@link DeviceRegistration} with the given {@link Device} if it exists, or {@code null} otherwise.
     */
    Optional<DeviceRegistration> findByDeviceAndActiveTrue(Device device);

    /**
     * Retrieves the active {@link DeviceRegistration} with the given {@link Device} and {@link User}.
     *
     * @param device The registered {@link Device}.
     * @param owner  The {@link User} owning the {@link Device}.
     * @return A <b>nullable</b> {@link Optional} of {@link DeviceRegistration}
     * containing the {@link DeviceRegistration} with the given {@link Device} if it exists, or {@code null} otherwise.
     */
    Optional<DeviceRegistration> findByDeviceAndOwnerAndActiveTrue(Device device, User owner);

    /**
     * Indicates whether an active {@link DeviceRegistration} exists for the given {@link Device}.
     *
     * @param device The registered {@link Device}.
     * @return {@code true} if the given {@code device} has an active registration, or {@code false} otherwise.
     */
    boolean existsByDeviceAndActiveTrue(Device device);

    /**
     * Indicates whether an active {@link DeviceRegistration} exists for the given {@link Device} and {@link User}.
     *
     * @param device The registered {@link Device}.
     * @param owner  The {@link User} owning the {@link Device}.
     * @return {@code true} if it exists, or {@code false} otherwise.
     */
    boolean existsByDeviceAndOwnerAndActiveTrue(Device device, User owner);

    /**
     * Indicates whether an active {@link DeviceRegistration} exists for the {@link Device}
     * with the given {@code deviceId}
     * and the {@link User} with the given {@code username}.
     *
     * @param deviceId The id of the registered {@link Device}.
     * @param username The username of the {@link User} owning the {@link Device}.
     * @return {@code true} if it exists, or {@code false} otherwise.
     */
    boolean existsByDeviceIdAndOwnerUsernameAndActiveTrue(long deviceId, String username);
}
