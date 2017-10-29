package ar.edu.itba.iot.carne_iot.server.persistence.daos;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.persistence.custom_repositories.ExtendedJpaRepository;

/**
 * Defines behaviour of the DAO in charge of managing {@link Device}s data.
 */
public interface DeviceDao extends ExtendedJpaRepository<Device, Long> {
}
