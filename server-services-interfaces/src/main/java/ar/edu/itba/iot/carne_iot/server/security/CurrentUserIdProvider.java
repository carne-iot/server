package ar.edu.itba.iot.carne_iot.server.security;

import ar.edu.itba.iot.carne_iot.server.exceptions.UnauthenticatedException;

import java.util.Optional;

/**
 * Defines behaviour for an object in charge of providing the current
 * {@link ar.edu.itba.iot.carne_iot.server.models.User} {@code id}
 * (i.e the authenticated user in the ongoing request).
 */
public interface CurrentUserIdProvider {


    /**
     * @return A nullable {@link Optional} containing the {@code id}
     * of the current {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * (i.e the authenticated user in the ongoing request).
     */
    Optional<Long> currentUserIdOptional();

    /**
     * @return The {@code id} of the current {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * (i.e the authenticated user in the on going request).
     * @throws UnauthenticatedException If no {@link ar.edu.itba.iot.carne_iot.server.models.User}
     *                                  is authenticated, or if the request is anonymous.
     */
    default long currentUserId() throws UnauthenticatedException {
        return currentUserIdOptional().orElseThrow(UnauthenticatedException::new);
    }
}
