package ar.edu.itba.iot.carne_iot.server.security;

import ar.edu.itba.iot.carne_iot.server.persistence.daos.DeviceRegistrationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link UserPermissionProvider}.
 */
@Component("devicePermissionProvider")
/* package */ class DevicePermissionProviderImpl implements DevicePermissionProvider {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(DevicePermissionProviderImpl.class);

    /**
     * A {@link DeviceRegistrationDao} used to retrieve
     * {@link ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration} to check ownership.
     */
    private final DeviceRegistrationDao deviceRegistrationDao;


    @Autowired
    /* package */ DevicePermissionProviderImpl(DeviceRegistrationDao deviceRegistrationDao) {
        this.deviceRegistrationDao = deviceRegistrationDao;
    }


    @Override
    public boolean isOwnerOrAdmin(long deviceId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        if (PermissionProviderHelper.isAdmin(authentication)) {
            return true;
        }

        final Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof String)) {
            LOGGER.error("An Authentication instance has reached the service layer " +
                    "having its principal being null or without having a String as a principal.");
            return false;
        }

        return deviceRegistrationDao.existsByDeviceIdAndOwnerUsernameAndActiveTrue(deviceId, (String) principal);
    }
}
