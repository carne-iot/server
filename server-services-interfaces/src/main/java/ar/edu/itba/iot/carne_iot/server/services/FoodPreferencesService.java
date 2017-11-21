package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.models.FoodPreference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Defines behaviour of the service in charge of managing {@link FoodPreference}s.
 */
public interface FoodPreferencesService {

    /**
     * List {@link FoodPreference}s belonging to a given {@link ar.edu.itba.iot.carne_iot.server.models.User},
     * in a paginated view.
     *
     * @param userId   The id of the {@link ar.edu.itba.iot.carne_iot.server.models.User}
     *                 whose {@link FoodPreference}s are being returned.
     * @param pageable The {@link Pageable} object containing pagination stuff.
     * @return The resulting {@link Page}.
     */
    Page<FoodPreference> findByOwner(long userId, Pageable pageable);

    /**
     * Gets a specific {@link FoodPreference} by it's {@code id}.
     *
     * @param id The {@link FoodPreference}'s id.
     * @return A <b>nullable</b> {@link Optional} of {@link FoodPreference}
     * containing the {@link FoodPreference} with the given {@code id} if it exists, or {@code null} otherwise.
     */
    Optional<FoodPreference> findById(long id);

    /**
     * Gets a {@link FoodPreference} with the given {@code name},
     * belonging to the given {@link ar.edu.itba.iot.carne_iot.server.models.User}.
     *
     * @param name   The {@link FoodPreference} name.
     * @param userId The id of the {@link ar.edu.itba.iot.carne_iot.server.models.User} owning the preference.
     * @return A <b>nullable</b> {@link Optional} of {@link FoodPreference}
     * containing the {@link FoodPreference} with the given {@code name},
     * belonging to the given {@link ar.edu.itba.iot.carne_iot.server.models.User},
     * if it exists, or {@code null} otherwise.
     */
    Optional<FoodPreference> findByNameAndOwner(String name, long userId);

    /**
     * Creates a new {@link FoodPreference}.
     *
     * @param name        The name for the new {@link FoodPreference}.
     * @param temperature The temperature for the new {@link FoodPreference}.
     * @param userId      The id of the {@link ar.edu.itba.iot.carne_iot.server.models.User}
     *                    that will own the new {@link FoodPreference}
     * @return The created {@link FoodPreference}.
     */
    FoodPreference create(String name, BigDecimal temperature, long userId);

    /**
     * Updates the {@link FoodPreference} with the given {@code preferenceId}.
     *
     * @param preferenceId The id of the {@link FoodPreference} to be updated.
     * @param name         The new name for the {@link FoodPreference}.
     * @param temperature  The new temperature for the {@link FoodPreference}.
     */
    void update(long preferenceId, String name, BigDecimal temperature);

    /**
     * Removes the {@link FoodPreference} with the given {@code preferenceId}.
     *
     * @param preferenceId The id of the {@link FoodPreference} to be removed.
     */
    void delete(long preferenceId);
}
