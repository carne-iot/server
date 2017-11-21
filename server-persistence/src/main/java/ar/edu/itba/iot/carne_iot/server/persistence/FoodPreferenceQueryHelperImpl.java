package ar.edu.itba.iot.carne_iot.server.persistence;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.FoodPreference;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.FoodPreferenceQueryHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link FoodPreferenceQueryHelper}.
 */
@Component
public class FoodPreferenceQueryHelperImpl implements FoodPreferenceQueryHelper {

    @Override
    public void validatePageable(Pageable pageable) throws InvalidPropertiesException {
        PersistenceHelper.validatePageable(pageable, FoodPreference.class);
    }
}
