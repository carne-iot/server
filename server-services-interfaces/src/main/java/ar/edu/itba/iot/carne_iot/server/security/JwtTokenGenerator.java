package ar.edu.itba.iot.carne_iot.server.security;

import ar.edu.itba.iot.carne_iot.server.models.User;

/**
 * Defines behaviour of an object that is in charge of generating JWTs.
 */
public interface JwtTokenGenerator {

    /**
     * Generates a token based on the given {@link User}.
     *
     * @param user The {@link User} to which the generated token belongs to.
     * @return The generated token together with its jti.
     */
    TokenAndSessionContainer generate(final User user);

    /**
     * Container class that wraps a token together with the session id it belongs to.
     */
    class TokenAndSessionContainer {

        /**
         * The authentication token.
         */
        private final String token;

        /**
         * The session id.
         */
        private final long jti;

        /**
         * @param token The authentication token.
         * @param jti   The session id.
         */
        public TokenAndSessionContainer(String token, long jti) {
            this.token = token;
            this.jti = jti;
        }

        /**
         * @return The authentication token.
         */
        public String getToken() {
            return token;
        }

        /**
         * @return The session id.
         */
        public long getJti() {
            return jti;
        }
    }
}
