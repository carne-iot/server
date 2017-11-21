package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationHelper;
import ar.edu.itba.iot.carne_iot.server.exceptions.InvalidCredentialsException;
import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;
import ar.edu.itba.iot.carne_iot.server.models.Session;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.SessionDao;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao;
import ar.edu.itba.iot.carne_iot.server.security.JwtTokenGenerator;
import ar.edu.itba.iot.carne_iot.server.security.PasswordValidatorImpl;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of {@link LoginService}.
 */
@Service
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService, ValidationExceptionThrower {

    /**
     * Amount of tries to perform the session creation
     */
    private static final int MAX_TRIES = 10;

    /**
     * DAO for retrieving {@link User}s data.
     */
    private final UserDao userDao;

    /**
     * DAO for retrieving {@link Session}s data.
     */
    private final SessionDao sessionDao;

    /**
     * {@link PasswordEncoder} used for hashing passwords when authenticating a {@link User}.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * A {@link JwtTokenGenerator} to create tokens when login is performed.
     */
    private final JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    public LoginServiceImpl(UserDao userDao, SessionDao sessionDao, PasswordEncoder passwordEncoder,
                            JwtTokenGenerator jwtTokenGenerator) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Override
    @Transactional
    public UserTokenAndJtiContainer login(String username, String password) throws InvalidCredentialsException {
        validateCredentials(username, password);
        final User user = userDao.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Unknown user"));
        final boolean validPassword = passwordEncoder.matches(password, user.getHashedPassword());
        if (!validPassword) {
            throw new InvalidCredentialsException("Password does not match");
        }
        Hibernate.initialize(user.getRoles());

        return createSession(user); // Tries to create the token, retrying if the jti is already in use
    }

    // ================================
    // Helpers
    // ================================

    /**
     * Validates the given credentials.
     *
     * @param username The username to be validated.
     * @param password The password to be validated.
     * @throws ValidationException If the credentials are not valid.
     */
    private void validateCredentials(String username, String password) throws ValidationException {
        final List<ValidationError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(username, errorList, MISSING_USERNAME);
        ValidationHelper.objectNotNull(password, errorList, PasswordValidatorImpl.MISSING_PASSWORD);

        throwValidationException(errorList);
    }

    /**
     * Tries to create a {@link Session}. Might fail if it already exists a {@link Session}
     * with the same session id for the given {@code user}. This is a rare situation.
     *
     * @param user The {@link User} to which the {@link Session} belongs to.
     * @return The token representing the {@link Session}.
     * @throws RuntimeException If the session could not be created.
     */
    private UserTokenAndJtiContainer createSession(User user) throws RuntimeException {
        boolean validSession = false;
        int tries = 0;
        JwtTokenGenerator.TokenAndSessionContainer container = null;
        while (!validSession && tries < MAX_TRIES) {
            container = jwtTokenGenerator.generateUserToken(user);
            validSession = !sessionDao.existsByOwnerAndJti(user, container.getJti());
            tries++;
        }
        if (tries >= MAX_TRIES) {
            throw new RuntimeException("Could not create a session after " + MAX_TRIES + "tries");
        }

        Objects.requireNonNull(container, "The container was not initialized correctly");
        final Session session = new Session(user, container.getJti());
        sessionDao.save(session);

        return new UserTokenAndJtiContainer(user, container.getToken(), container.getJti());
    }

    private static final ValidationError MISSING_USERNAME =
            new ValidationError(ValidationError.ErrorCause.MISSING_VALUE, "The username is missing", "username");
}
