package ar.edu.itba.iot.carne_iot.server.web.security.authentication;

import ar.edu.itba.iot.carne_iot.server.models.Role;
import org.springframework.security.core.Authentication;

import java.util.Set;

/**
 * An {@link Authentication} created with a JWT token
 * for a given {@link ar.edu.itba.iot.carne_iot.server.models.Device}.
 * Note: This token saves the deviceId as the token details.
 */
/* package */ class DeviceJwtAuthenticationToken extends JwtAuthenticationToken {

    /**
     * @param roles    The roles this token has.
     * @param deviceId The {@code id} of the {@link ar.edu.itba.iot.carne_iot.server.models.Device}
     *                 that this token belongs to.
     */
    DeviceJwtAuthenticationToken(Set<Role> roles, long deviceId) {
        super(roles);
        this.setDetails(deviceId);
    }
}
