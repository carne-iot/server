package ar.edu.itba.iot.carne_iot.server.persistence.daos;

import ar.edu.itba.iot.carne_iot.server.models.FoodPreference;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.custom_repositories.ExtendedJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Defines behaviour of the DAO in charge of managing {@link FoodPreference}s data.
 */
public interface FoodPreferenceDao extends ExtendedJpaRepository<FoodPreference, Long> {

    /**
     * Retrieves a {@link Page} of {@link FoodPreference}s belonging to the given {@code user},
     * according to the given {@code pageable}.
     *
     * @param user The {@link User} owning the resultant {@link FoodPreference}s.
     * @return The resultant {@link Page}.
     */
    Page<FoodPreference> findByOwner(User user, Pageable pageable);

    /**
     * Retrieves the {@link FoodPreference} with the given {@code name}, belonging to the given {@link User}.
     *
     * @param name The {@link FoodPreference} name.
     * @param user The {@link User} owning the preference.
     * @return A <b>nullable</b> {@link Optional} of {@link FoodPreference}
     * containing the {@link FoodPreference} with the given {@code name}, belonging to the given {@link User},
     * if it exists, or {@code null} otherwise.
     */
    Optional<FoodPreference> findByNameAndOwner(String name, User user);

    /**
     * Indicates if a given {@link User} has a {@link FoodPreference} with a given {@code name}.
     *
     * @param name The {@link FoodPreference} name.
     * @param user The {@link User} owning the preference.
     * @return {@code true} if the {@link User} has the preference, or {@code false} otherwise.
     */
    boolean existsByNameAndOwner(String name, User user);
}
