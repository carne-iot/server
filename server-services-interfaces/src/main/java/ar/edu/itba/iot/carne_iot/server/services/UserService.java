package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.models.Role;
import ar.edu.itba.iot.carne_iot.server.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/**
 * Defines behaviour of the service in charge of managing {@link User}s.
 */
public interface UserService {

    /**
     * Finds stored {@link User}s, applying optional filters and pagination.
     * String filters are compared with the "like" keyword, matching anywhere.
     *
     * @param fullName     A filter for the {@link User}'s full name.
     * @param minBirthDate The minimum age of the retrieved {@link User}s
     * @param maxBirthDate The maximum age of the retrieved {@link User}s
     * @param username     A filter for the {@link User}'s username.
     * @param email        A filter for the {@link User}'s email.
     * @param pageable     An {@link Object} implementing the {@link Pageable} interface.
     * @return The resulting {@link Page}.
     * @apiNote Those parameter that are {@code null} must not be taken into account (they are optional).
     */
    Page<User> findMatching(String fullName, LocalDate minBirthDate, LocalDate maxBirthDate,
                            String username, String email, Pageable pageable);

    /**
     * Gets a specific {@link User} by it's {@code id}.
     *
     * @param id The {@link User}'s id.
     * @return A <b>nullable</b> {@link Optional} of {@link User}
     * containing the {@link User} with the given {@code id} if it exists, or {@code null} otherwise.
     */
    Optional<User> getById(long id);

    /**
     * Retrieves the {@link User} with the given {@code username}.
     *
     * @param username The {@link User}'s username.
     * @return A <b>nullable</b> {@link Optional} of {@link User}
     * containing the {@link User} with the given {@code username} if it exists, or {@code null} otherwise.
     */
    Optional<User> getByUsername(String username);

    /**
     * Retrieves the {@link User} with the given {@code email}.
     *
     * @param email The {@link User}'s email.
     * @return A <b>nullable</b> {@link Optional} of {@link User}
     * containing the {@link User} with the given {@code email} if it exists, or {@code null} otherwise.
     */
    Optional<User> getByEmail(String email);

    /**
     * Creates a new {@link User}.
     *
     * @param fullName  The {@link User}'s full name.
     * @param birthDate The {@link User}'s birth date.
     * @param username  The {@link User}'s username.
     * @param email     The {@link User}'s email.
     * @param password  The {@link User}'s password.
     * @return The new {@link User}.
     */
    User register(String fullName, LocalDate birthDate, String username, String email, String password);

    /**
     * Updates the {@link User} with the given {@code id}.
     *
     * @param id           The {@link User}'s id.
     * @param newFullName  The new full name.
     * @param newBirthDate The new birth date.
     */
    void update(long id, String newFullName, LocalDate newBirthDate);

    /**
     * Changes the username to the {@link User} with the given {@code id}.
     *
     * @param id          The {@link User}'s id.
     * @param newUsername The new username.
     */
    void changeUsername(long id, String newUsername);

    /**
     * Changes the email to the {@link User} with the given {@code id}.
     *
     * @param id       The {@link User}'s id.
     * @param newEmail The new email.
     */
    void changeEmail(long id, String newEmail);

    /**
     * Changes the password to the {@link User} with the given {@code id}.
     *
     * @param id              The {@link User}'s id.
     * @param currentPassword The {@link User}'s old password.
     * @param newPassword     The new password.
     */
    void changePassword(long id, String currentPassword, String newPassword);

    /**
     * Retrieves the {@link User}'s with the given {@code id} {@link Role}s.
     *
     * @param id The id of the {@link User} whose roles must be returned.
     * @return The {@link Set} of {@link Role}s of the given {@link User}.
     */
    Set<Role> getRoles(long id);

    /**
     * Adds the given {@link Role} to the {@link User} with th given {@code id}.
     *
     * @param id   The id of the {@link User} to which the given {@link Role} must be added.
     * @param role The {@link Role} to be added.
     * @apiNote This is an idempotent method.
     */
    void addRole(long id, Role role);

    /**
     * Removes the given {@link Role} to the {@link User} with th given {@code id}.
     *
     * @param id   The id of the {@link User} to which the given {@link Role} must be removed.
     * @param role The {@link Role} to be removed.
     * @apiNote This is an idempotent method.
     */
    void removeRole(long id, Role role);

    /**
     * Deletes the {@link User} with the given {@code id}.
     * This is an idempotent operation.
     *
     * @param id The id of the {@link User} to be deleted.
     */
    void deleteById(long id);

    /**
     * Deletes the {@link User} with the given {@code username}.
     * This is an idempotent operation.
     *
     * @param username The username of the {@link User} to be deleted.
     */
    void deleteByUsername(String username);

    /**
     * Deletes the {@link User} with the given {@code email}.
     * This is an idempotent operation.
     *
     * @param email The email of the {@link User} to be deleted.
     */
    void deleteByEmail(String email);
}
