package ar.edu.itba.iot.carne_iot.server.security;

import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * Concrete implementation of {@link UserPermissionProvider}.
 */
@Component("userPermissionProvider")
/* package */ class UserPermissionProviderImpl implements UserPermissionProvider {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(UserPermissionProviderImpl.class);

    /**
     * A {@link UserDao} used to retrieve a {@link User} by its id.
     */
    private final UserDao userDao;


    @Autowired
    /* package */ UserPermissionProviderImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public boolean readById(long id) {
        return performAuthorization(userDao::findById, id);
    }

    @Override
    public boolean readByUsername(String username) {
        return performAuthorization(userDao::findByUsername, username);
    }

    @Override
    public boolean readByEmail(String email) {
        return performAuthorization(userDao::findByEmail, email);
    }

    @Override
    public boolean writeById(long id) {
        return performAuthorization(userDao::findById, id);
    }

    @Override
    public boolean deleteById(long id) {
        return performAuthorization(userDao::findById, id);
    }

    @Override
    public boolean deleteByUsername(String username) {
        return performAuthorization(userDao::findByUsername, username);
    }

    @Override
    public boolean deleteByEmail(String email) {
        return performAuthorization(userDao::findByEmail, email);
    }

    /**
     * Performs authorization over the {@link User} contained in the {@link Optional}
     * retrieved by the given {@code userGetterFunction}, which takes the given {@code searchCriteria}.
     * The one performing the operation is the {@link User} whose {@code username} is stored
     * in the {@link Authentication} retrieved by the {@link SecurityContextHolder#getContext()} method.
     *
     * @param userGetterFunction {@link Function} that takes a {@code T} an returns an {@link Optional} of {@link User}.
     *                           This {@link Function} is used to query the {@link User}
     *                           to which the operation is going to be applied.
     * @param searchCriteria     The {@link User} search criteria
     *                           (i.e the argument for the given {@code userGetterFunction}).
     * @param <T>                The concrete type for the {@code searchCriteria} element.
     * @return {@code true} if it is authorized, or {@code false} otherwise.
     */
    private static <T> boolean performAuthorization(Function<T, Optional<User>> userGetterFunction, T searchCriteria) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        if (PermissionProviderHelper.isAdmin(authentication)) {
            return true;
        }

        final Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof String)) {
            LOGGER.error("An Authentication instance has reached the service layer " +
                    "having its principal being null or without having a String as a principal.");
            return false;
        }

        return userGetterFunction.apply(searchCriteria)
                .map(user -> user.getUsername().equals(principal))
                .orElse(false);
    }
}
