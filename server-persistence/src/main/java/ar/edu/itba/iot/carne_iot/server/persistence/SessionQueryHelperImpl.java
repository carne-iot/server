package ar.edu.itba.iot.carne_iot.server.persistence;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.Session;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.SessionQueryHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of a {@link SessionQueryHelper}.
 */
@Component
public class SessionQueryHelperImpl implements SessionQueryHelper {

    @Override
    public void validatePageable(Pageable pageable) throws InvalidPropertiesException {
        PersistenceHelper.validatePageable(pageable, Session.class);
    }
}
