package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.UniqueViolationError;
import ar.edu.itba.iot.carne_iot.server.exceptions.NoSuchEntityException;
import ar.edu.itba.iot.carne_iot.server.exceptions.UniqueViolationException;
import ar.edu.itba.iot.carne_iot.server.models.FoodPreference;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.FoodPreferenceDao;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.FoodPreferenceQueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

/**
 * Concrete implementation of {@link FoodPreferencesService}.
 */
@Service
public class FoodPreferenceServiceImpl implements FoodPreferencesService {

    /**
     * A {@link UserDao} to retrieve {@link User} data.
     */
    private final UserDao userDao;

    /**
     * A {@link FoodPreferenceDao} to retrieve {@link FoodPreference} data.
     */
    private final FoodPreferenceDao foodPreferenceDao;

    /**
     * A {@link FoodPreferenceQueryHelper} to assist look up of {@link FoodPreference} by the {@link FoodPreferenceDao}.
     */
    private final FoodPreferenceQueryHelper foodPreferenceQueryHelper;

    @Autowired
    public FoodPreferenceServiceImpl(UserDao userDao, FoodPreferenceDao foodPreferenceDao,
                                     FoodPreferenceQueryHelper foodPreferenceQueryHelper) {
        this.userDao = userDao;
        this.foodPreferenceDao = foodPreferenceDao;
        this.foodPreferenceQueryHelper = foodPreferenceQueryHelper;
    }


    @Override
    @PreAuthorize("@userPermissionProvider.readById(#userId)")
    public Page<FoodPreference> findByOwner(long userId, Pageable pageable) {
        final User user = userDao.findById(userId).orElseThrow(NoSuchEntityException::new);
        foodPreferenceQueryHelper.validatePageable(pageable);

        return foodPreferenceDao.findByOwner(user, pageable);
    }

    @Override
    @PreAuthorize("@userPermissionProvider.readById(#userId)")
    public Optional<FoodPreference> findByNameAndOwner(String name, long userId) {
        final User user = userDao.findById(userId).orElseThrow(NoSuchEntityException::new);

        return foodPreferenceDao.findByNameAndOwner(name, user);
    }

    @Override
    @PreAuthorize("@userPermissionProvider.writeById(#userId)")
    public FoodPreference create(String name, BigDecimal temperature, long userId) {
        final User user = userDao.findById(userId).orElseThrow(NoSuchEntityException::new);
        validateNameUniqueness(name, user);
        final FoodPreference foodPreference = new FoodPreference(name, temperature, user);
        foodPreferenceDao.save(foodPreference);

        return foodPreference;
    }

    @Override
    @PreAuthorize("@userPermissionProvider.writeById(#ownerId)")
    public void update(long ownerId, String actualName, String newName, BigDecimal newTemperature) {
        final User user = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);
        final FoodPreference foodPreference = foodPreferenceDao.findByNameAndOwner(actualName, user)
                .orElseThrow(NoSuchEntityException::new);
        if (newName != null && !newName.equals(foodPreference.getName())) {
            validateNameUniqueness(newName, foodPreference.getOwner());
        }
        foodPreference.update(newName, newTemperature);
        foodPreferenceDao.save(foodPreference);
    }

    @Override
    @PreAuthorize("@userPermissionProvider.deleteById(#ownerId)")
    public void delete(long ownerId, String name) {
        final User user = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);
        foodPreferenceDao.findByNameAndOwner(name, user).ifPresent(foodPreferenceDao::delete);
    }

    /**
     * Validates that the given {@link User} can store a {@link FoodPreference} with the given {@code name}
     * (i.e checks if the {@link User} does not contain another {@link FoodPreference} with the given name).
     *
     * @param name The name for the {@link FoodPreference} to be validated.
     * @param user The {@link User} whose {@link FoodPreference} must be checked.
     * @throws UniqueViolationException If the {@link User} contains a {@link FoodPreference}
     *                                  with the given {@code name}.
     */
    private void validateNameUniqueness(String name, User user) throws UniqueViolationException {
        if (foodPreferenceDao.existsByNameAndOwner(name, user)) {
            throw new UniqueViolationException(Collections.singletonList(NAME_ALREADY_IN_USE));
        }
    }

    private static final UniqueViolationError NAME_ALREADY_IN_USE =
            new UniqueViolationError("The name for the food preference is already in use",
                    "name", "userId");
}
