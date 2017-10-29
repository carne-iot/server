package ar.edu.itba.iot.carne_iot.server.persistence;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.DeviceQueryHelper;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.UserQueryHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Concrete implementation of a {@link DeviceQueryHelper}.
 */
@Component
public class DeviceQueryHelperImpl implements DeviceQueryHelper {

    @Override
    public void validatePageable(Pageable pageable) throws InvalidPropertiesException {
        PersistenceHelper.validatePageable(pageable, Device.class);
    }
}
