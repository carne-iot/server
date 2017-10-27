package ar.edu.itba.iot.carne_iot.server.web.security.authentication;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception to be thrown in case the set authentication scheme is not supported.
 */
/* package */ class UnsupportedAuthenticationSchemeException extends AuthenticationException {

    /**
     * Default constructor.
     */
    UnsupportedAuthenticationSchemeException(String scheme) {
        super("The set scheme is not supported");
    }
}
