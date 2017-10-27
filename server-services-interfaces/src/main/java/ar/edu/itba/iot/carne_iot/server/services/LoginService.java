package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidCredentialsException;
import ar.edu.itba.iot.carne_iot.server.models.User;

/**
 * Defines behaviour of the service in charge of managing login
 * (i.e creating {@link ar.edu.itba.iot.carne_iot.server.models.Session}s).
 */
public interface LoginService {

    /**
     * Performs the login process (i.e username and password authentication),
     * returning a token that represents the session.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return A JWT in string representation.
     * @throws InvalidCredentialsException In cae the user does not exists, or if the password is not the correct one.
     */
    UserTokenAndJtiContainer login(String username, String password) throws InvalidCredentialsException;

    /**
     * Class wrapping a {@link User} and a token (in {@link String} representation) for it.
     */
    class UserTokenAndJtiContainer {

        /**
         * The wrapped user.
         */
        private final User user;
        /**
         * The token belonging to the {@link User}.
         */
        private final String token;

        /**
         * The JWT id of the token.
         */
        private final long jti;

        /**
         * Constructor.
         *
         * @param user  The wrapped user.
         * @param token The token belonging to the {@link User}.
         * @param jti   The JWT id of the token.
         */
        /* package */ UserTokenAndJtiContainer(User user, String token, long jti) {
            this.user = user;
            this.token = token;
            this.jti = jti;
        }

        /**
         * @return The wrapped user.
         */
        public User getUser() {
            return user;
        }

        /**
         * @return The token belonging to the {@link User}.
         */
        public String getToken() {
            return token;
        }

        /**
         * @return The JWT id of the token.
         */
        public long getJti() {
            return jti;
        }
    }
}
