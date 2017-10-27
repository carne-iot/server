package ar.edu.itba.iot.carne_iot.server.persistence.daos;

import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.custom_repositories.ExtendedJpaRepository;

import java.util.Optional;

/**
 * Defines behaviour of the DAO in charge of managing {@link User}s data.
 */
public interface UserDao extends ExtendedJpaRepository<User, Long> {

    /**
     * Retrieves the {@link User} with the given {@code username}.
     *
     * @param username The {@link User}'s username.
     * @return A <b>nullable</b> {@link Optional} of {@link User}
     * containing the {@link User} with the given {@code username} if it exists, or {@code null} otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves the {@link User} with the given {@code email}.
     *
     * @param email The {@link User}'s email.
     * @return A <b>nullable</b> {@link Optional} of {@link User}
     * containing the {@link User} with the given {@code email} if it exists, or {@code null} otherwise.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a {@link User} exists with the given {@code username}.
     *
     * @param username The username to check if a {@link User} exists with.
     * @return {@code true} if a {@link User} exists with the given {@code username}, or {@code false} otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a {@link User} exists with the given {@code email}.
     *
     * @param email The email to check if a {@link User} exists with.
     * @return {@code true} if a {@link User} exists with the given {@code email}, or {@code false} otherwise.
     */
    boolean existsByEmail(String email);
}
