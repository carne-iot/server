package ar.edu.itba.iot.carne_iot.server.persistence.query_helpers;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidPropertiesException;
import ar.edu.itba.iot.carne_iot.server.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Defines behaviour of an object in charge of helping the task of querying {@link User}s
 * by a {@link ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao}.
 */
public interface UserQueryHelper {

    /**
     * Creates a new {@link Specification} of {@link User} used to query them according to the given parameters,
     * applying ANDs between them.
     * String parameters are compared with the "like" keyword, matching anywhere.
     *
     * @param fullName     A filter for the {@link User}'s full name.
     * @param minBirthDate The minimum age of the retrieved {@link User}s
     * @param maxBirthDate The maximum age of the retrieved {@link User}s
     * @param username     A filter for the {@link User}'s username.
     * @param email        A filter for the {@link User}'s email.
     * @return The {@link Specification} of {@link User}
     * that can be used to get those {@link User}s matching the give parameters.
     * @apiNote Those parameter that are {@code null} must not be taken into account in the {@link Specification}.
     */
    Specification<User> createUserSpecification(String fullName, LocalDate minBirthDate, LocalDate maxBirthDate,
                                                String username, String email);

    /**
     * Validates that the given {@link Pageable} is valid for querying {@link User}s.
     *
     * @param pageable The {@link Pageable} to be validated.
     * @throws InvalidPropertiesException If it has a {@link org.springframework.data.domain.Sort}
     *                                    with invalid properties.
     */
    void validatePageable(Pageable pageable) throws InvalidPropertiesException;
}
