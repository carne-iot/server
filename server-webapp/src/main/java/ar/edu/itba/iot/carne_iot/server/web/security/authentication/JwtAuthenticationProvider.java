package ar.edu.itba.iot.carne_iot.server.web.security.authentication;

import ar.edu.itba.iot.carne_iot.server.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * {@link AuthenticationProvider} in charge of performing JWT authentication.
 */
@Component
public final class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtCompiler jwtCompiler;

    @Autowired
    public JwtAuthenticationProvider(JwtCompiler jwtCompiler) {
        this.jwtCompiler = jwtCompiler;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "The authentication must not be null");
        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication,
                "The authentication must be a JWTAuthenticationToken");

        final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        try {
            // Performs all validations to the token
            final JwtCompiler.JwtTokenData tokenData = jwtCompiler.compile(jwtAuthenticationToken.getRawToken());

            // We create a new token with the needed data (username, roles, etc.)
            final JwtAuthenticationToken resultToken = createToken(tokenData);
            resultToken.setPrincipal(tokenData.getUsername());
            resultToken.setUserId(tokenData.getUserId());
            resultToken.authenticate();

            return resultToken;
        } catch (JwtException e) {
            throw new FailedJwtAuthenticationException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * Creates a {@link JwtAuthenticationToken} according to the given {@link JwtCompiler.JwtTokenData}.
     *
     * @param tokenData The {@link JwtCompiler.JwtTokenData} from which token information will be taken.
     * @return The created {@link JwtAuthenticationToken}.
     */
    private JwtAuthenticationToken createToken(JwtCompiler.JwtTokenData tokenData) {
        final Set<Role> roles = tokenData.getRoles();
        if (tokenData instanceof JwtCompiler.DeviceJwtTokenData) {
            return new DeviceJwtAuthenticationToken(roles, ((JwtCompiler.DeviceJwtTokenData) tokenData).getDeviceId());
        }
        return new JwtAuthenticationToken(roles);
    }
}
