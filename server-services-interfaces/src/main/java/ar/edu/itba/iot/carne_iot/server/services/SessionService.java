package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.models.Session;
import ar.edu.itba.iot.carne_iot.server.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Defines behaviour of the service in charge of managing sessions.
 * This service can't create {@link Session}s. That is a {@link LoginService} task.
 */
public interface SessionService {

    /**
     * Retrieves a {@link Page} of {@link Session}s belonging to the given {@code owner},
     * according to the given {@code pageable}.
     *
     * @param owner    The {@link User} owning the resultant {@link Session}s.
     * @param pageable The {@link Pageable} used to set page stuff.
     * @return The resultant {@link Page}.
     */
    Page<Session> listSessions(User owner, Pageable pageable);

    /**
     * Indicates whether a {@link Session} is valid (i.e not blacklisted).
     *
     * @param ownerId The id of the {@link User} owning the {@link Session}.
     * @param jti     The unique identifier for the {@link Session}.
     * @return {@code true} if the {@link Session} is valid, or {@code false} otherwise.
     */
    boolean validSession(long ownerId, long jti);

    /**
     * Invalidates a {@link Session} (i.e blacklists it).
     *
     * @param ownerId The id of the {@link User} owning the {@link Session}.
     * @param jti     The unique identifier for the {@link Session}.
     */
    void invalidateSession(long ownerId, long jti);

    /**
     * Retrieves the current {@link User} (i.e the currently authenticated {@link User}).
     *
     * @return A nullable {@link Optional} of {@link User}, containing the currently authenticated {@link User}.
     * @apiNote If the {@link Optional} is empty, there is no currently authenticated {@link User}.
     */
    Optional<User> getCurrentUser();
}
