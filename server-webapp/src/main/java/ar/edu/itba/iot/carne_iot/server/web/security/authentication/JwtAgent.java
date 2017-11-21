package ar.edu.itba.iot.carne_iot.server.web.security.authentication;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.Role;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.security.JwtTokenGenerator;
import ar.edu.itba.iot.carne_iot.server.services.SessionService;
import io.jsonwebtoken.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Object in charge of managing JWT operations (generation and validation).
 */
@Component
/* package */ class JwtAgent implements JwtTokenGenerator, JwtCompiler {

    private final static String USER_ID_CLAIM_NAME = "uid";

    private final static String JWT_ID_CLAIM_NAME = "jti";

    private final static String ROLES_CLAIM_NAME = "roles";

    private final static String DEVICE_ID_CLAIMS_NAME = "did";

    /**
     * {@link SessionService} used to check if a given token is valid.
     */
    private final SessionService sessionService;

    /**
     * The secret key used to sign the tokens, encoded in base 64.
     */
    private final String base64EncodedSecretKey;

    /**
     * The duration of tokens, in milliseconds.
     */
    private final long duration;

    /**
     * THe signing algorithm used to sign tokens.
     */
    private final SignatureAlgorithm signatureAlgorithm;

    /**
     * Constructor.
     *
     * @param secretKey      The secret key used to sign the tokens
     * @param duration       The duration of tokens, in seconds
     * @param sessionService The {@link SessionService} used to create and check validity of a JWT.
     */
    /* package */ JwtAgent(@Value("${custom.security.jwt.signing-key}") String secretKey,
                           @Value("${custom.security.jwt.duration}") Long duration,
                           SessionService sessionService) {
        this.base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.duration = duration * 1000;
        this.sessionService = sessionService;
        this.signatureAlgorithm = SignatureAlgorithm.HS512;
    }

    @Override
    public TokenAndSessionContainer generateUserToken(User user) {
        Objects.requireNonNull(user, "The user must not be null");
        if (!Hibernate.isInitialized(user.getRoles())) {
            throw new IllegalStateException("The user is not initialized correctly");
        }
        final long jti = new SecureRandom().nextLong();

        final Claims claims = Jwts.claims();
        claims.put(USER_ID_CLAIM_NAME, user.getId());
        claims.put(JWT_ID_CLAIM_NAME, jti);
        claims.put(ROLES_CLAIM_NAME, user.getRoles());
        claims.setSubject(user.getUsername());
        final Date now = new Date();

        final String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + duration))
                .signWith(signatureAlgorithm, base64EncodedSecretKey)
                .compact();

        return new TokenAndSessionContainer(token, jti);
    }

    @Override
    public TokenAndSessionContainer generateDeviceToken(User user, Device device) {
        Objects.requireNonNull(user, "The user must not be null");
        Objects.requireNonNull(device, "The device must not be null");
        final long jti = new SecureRandom().nextLong();

        final Claims claims = Jwts.claims();
        claims.put(USER_ID_CLAIM_NAME, user.getId());
        claims.put(JWT_ID_CLAIM_NAME, jti);
        claims.put(ROLES_CLAIM_NAME, Collections.singleton(Role.ROLE_DEVICE));
        claims.put(DEVICE_ID_CLAIMS_NAME, device.getId());
        claims.setSubject(user.getUsername());
        final Date now = new Date();

        final String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(Long.MAX_VALUE)) // Will expire after the world is ended
                .signWith(signatureAlgorithm, base64EncodedSecretKey)
                .compact();

        return new TokenAndSessionContainer(token, jti);
    }

    @Override
    public JwtTokenData compile(String rawToken) throws IllegalArgumentException {
        if (!StringUtils.hasText(rawToken)) {
            throw new IllegalArgumentException("The token must not be null or empty");
        }

        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(base64EncodedSecretKey)
                    .parse(rawToken, CustomJwtHandlerAdapter.getInstance())
                    .getBody();

            // Previous step validated the following values
            final long userId = (long) claims.get(USER_ID_CLAIM_NAME);
            final long jti = (long) claims.get(JWT_ID_CLAIM_NAME);
            @SuppressWarnings("unchecked") final Set<Role> roles = (Set<Role>) claims.get(ROLES_CLAIM_NAME);
            final String username = claims.getSubject();

            if (roles.contains(Role.ROLE_DEVICE)) {
                final long deviceId = (long) claims.get(DEVICE_ID_CLAIMS_NAME);
                return new DeviceJwtTokenData(userId, username, roles, deviceId);
            }
            checkJwtBlacklist(userId, jti);  // Device tokens are not blacklisted
            return new JwtTokenData(userId, username, roles);
        } catch (MalformedJwtException | SignatureException | ExpiredJwtException | UnsupportedJwtException
                | MissingClaimException e) {
            throw new JwtException("There was a problem with the jwt token", e);
        }
    }


    /**
     * Validates the JWT identified by the given {@code userId} and {@code jti}
     *
     * @param userId The id of the {@link User} that owns the JWT.
     * @param jti    The JWT id.
     * @throws JwtException If the JWT is blacklisted.
     */
    private void checkJwtBlacklist(long userId, long jti) throws JwtException {
        if (sessionService.validSession(userId, jti)) {
            return;
        }
        throw new JwtException("Blacklisted JWT");
    }


    /**
     * Custom implementation of {@link JwtHandlerAdapter}.
     */
    private static class CustomJwtHandlerAdapter extends JwtHandlerAdapter<Jws<Claims>> {

        /**
         * Single instance of this class.
         */
        private static CustomJwtHandlerAdapter singleton = new CustomJwtHandlerAdapter();

        /**
         * @return The single instance of this class.
         */
        private static CustomJwtHandlerAdapter getInstance() {
            return singleton;
        }


        @Override
        public Jws<Claims> onClaimsJws(Jws<Claims> jws) {
            final JwsHeader<?> header = jws.getHeader();
            final Claims claims = jws.getBody();

            // Check user id is not missing
            final Object userIdObject = claims.get(USER_ID_CLAIM_NAME);
            if (userIdObject == null) {
                throw new MissingClaimException(header, claims, "Missing \"user id\" claim");
            }
            // Check user id is an integer (or long) number
            if (!(userIdObject instanceof Integer) && !(userIdObject instanceof Long)) {
                throw new MalformedJwtException("The \"user id\" claim must be an integer or a long");
            }
            // Transform the user id from integer to long
            if (userIdObject instanceof Integer) {
                final long userId = ((Integer) userIdObject).longValue();
                claims.put(USER_ID_CLAIM_NAME, userId);
            }

            // Check jti is not missing
            final Object jtiObject = claims.get(JWT_ID_CLAIM_NAME);
            if (jtiObject == null) {
                throw new MissingClaimException(header, claims, "Missing \"jwt id\" claim");
            }
            // Check user id is an integer (or long) number
            if (!(jtiObject instanceof Integer) && !(jtiObject instanceof Long)) {
                throw new MalformedJwtException("The \"jwt id\" claim must be an integer or a long");
            }
            // Transform the user id from integer to long
            if (jtiObject instanceof Integer) {
                final long jti = ((Integer) jtiObject).longValue();
                claims.put(JWT_ID_CLAIM_NAME, jti);
            }

            // Check roles is not missing
            final Object rolesObject = claims.get(ROLES_CLAIM_NAME);
            if (rolesObject == null) {
                throw new MissingClaimException(header, claims, "Missing \"roles\" claim");
            }
            // Check roles is a Collection
            if (!(rolesObject instanceof Collection)) {
                throw new MalformedJwtException("The \"roles\" claim must be a collection");
            }
            // Transform the collection into a Set of Role
            @SuppressWarnings("unchecked") final Collection<String> rolesCollection = (Collection<String>) rolesObject;
            claims.put(ROLES_CLAIM_NAME, rolesCollection.stream().map(Role::valueOf).collect(Collectors.toSet()));

            // Check issued at date is present and it is not a future date
            final Date issuedAt = Optional.ofNullable(claims.getIssuedAt())
                    .orElseThrow(() ->
                            new MissingClaimException(header, claims, "Missing \"issued at\" date"));
            if (issuedAt.after(new Date())) {
                throw new MalformedJwtException("The \"issued at\" date is a future date");
            }
            // Check expiration date is not missing
            if (claims.getExpiration() == null) {
                throw new MissingClaimException(header, claims, "Missing \"expiration\" date");
            }

            return jws;
        }
    }
}
