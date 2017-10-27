package ar.edu.itba.iot.carne_iot.server.web.security.authentication;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.iot.carne_iot.server.web.security.authentication.AuthenticationConstants.AUTHENTICATION_HEADER;
import static ar.edu.itba.iot.carne_iot.server.web.security.authentication.AuthenticationConstants.AUTHENTICATION_SCHEME;

/**
 * {@link AbstractAuthenticationProcessingFilter} in charge of performing JWT authentication.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * A {@link RequestMatcher} that will tell if a request does not require JWT authentication.
     */
    private final RequestMatcher optionalAuthenticationMatcher;

    /**
     * Default constructor.
     *
     * @param passwordAuthenticationEndpoint          The {@link RequestMatcher} for the endpoint
     *                                                to which password authentication is performed against to.
     * @param optionalAuthenticationEndpointsMatchers {@link List} of additional {@link RequestMatcher}
     *                                                matching endpoints in which JWT authentication is not required.
     */
    public JwtAuthenticationFilter(RequestMatcher passwordAuthenticationEndpoint,
                                   List<RequestMatcher> optionalAuthenticationEndpointsMatchers,
                                   AuthenticationFailureHandler tokenAuthenticationFailureHandler) {
        super("/**");
        Assert.notNull(passwordAuthenticationEndpoint, "A password authentication endpoint matcher must be set");
        optionalAuthenticationEndpointsMatchers =
                Optional.ofNullable(optionalAuthenticationEndpointsMatchers)
                        .orElse(new LinkedList<>());
        optionalAuthenticationEndpointsMatchers.add(passwordAuthenticationEndpoint);

        this.optionalAuthenticationMatcher = new OrRequestMatcher(optionalAuthenticationEndpointsMatchers);
        this.setAuthenticationFailureHandler(tokenAuthenticationFailureHandler);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
            // Do nothing
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final String authorizationHeader = request.getHeader(AUTHENTICATION_HEADER);

        if (!StringUtils.hasText(authorizationHeader)) {
            // Anonymous request. Continue only if it matches the optionalAuthenticationMatcher
            if (optionalAuthenticationMatcher.matches(request)) {
                return new AnonymousAuthenticationToken("ANONYMOUS", "ANONYMOUS",
                        Collections.singletonList(new SimpleGrantedAuthority("ANONYMOUS")));
            }
            throw new UnsupportedAnonymousAuthenticationException();
        }

        // If reached here, authentication credentials are present and, at least, the scheme is present
        final String[] credentials = authorizationHeader.split(" ");
        // We must check the scheme
        final String scheme = credentials[0];
        if (!AUTHENTICATION_SCHEME.equals(scheme)) {
            throw new UnsupportedAuthenticationSchemeException(AUTHENTICATION_HEADER);
        }

        // If reached here, the scheme is supported. We must check the token is present
        if (credentials.length <= 1) {
            throw new MissingJwtException();
        }

        // If reached here, the token is present. We assume everything is well formed (token is just one "word").
        final String jwtRawToken = credentials[1];

        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(jwtRawToken));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // Continue with normal flow
        chain.doFilter(request, response);
    }
}
