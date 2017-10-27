package ar.edu.itba.iot.carne_iot.server.persistence.daos;

import ar.edu.itba.iot.carne_iot.server.models.Session;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.custom_repositories.ExtendedJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Defines behaviour of the DAO in charge of managing {@link Session}s data.
 * Note that a {@link Session} has an {@code id}, but it also contains a {@code jti}.
 * Two {@link Session}s can have the same {@code jti} as long as they don't belong to the same {@link User}.
 */
public interface SessionDao extends ExtendedJpaRepository<Session, Long> {

    /**
     * Retrieves a {@link Page} of {@link Session}s belonging to the given {@code owner},
     * according to the given {@code pageable}.
     *
     * @param owner    The {@link User} owning the resultant {@link Session}s.
     * @param pageable The {@link Pageable} used to set page stuff.
     * @return The resultant {@link Page}.
     */
    Page<Session> findByOwner(User owner, Pageable pageable);

    /**
     * Retrieves a single {@link Session} that belongs to the {@link User} with the given {@code ownerId},
     * and whose session id is the given {@code jti}.
     *
     * @param ownerId The id of the {@link User} owning the {@link Session}.
     * @param jti     The unique identifier for the {@link Session}.
     * @return A nullable {@link Optional} with the {@link Session}
     * (can be empty if no {@link User} with the given {@code ownerId} exists,
     * or if the given {@link User} does not have a {@link Session} with the given {@code jti}).
     */
    Optional<Session> findByOwnerIdAndJti(long ownerId, long jti);

    /**
     * Indicates whether a {@link Session} exists, belonging to the given {@link User},
     * and with the given {@code jti}
     *
     * @param owner The id of the {@link User} owning the {@link Session}.
     * @param jti   The unique identifier for the {@link Session}.
     * @return {@code true} if the {@link Session} exists, or {@code false} otherwise.
     */
    boolean existsByOwnerAndJti(User owner, long jti);
}
