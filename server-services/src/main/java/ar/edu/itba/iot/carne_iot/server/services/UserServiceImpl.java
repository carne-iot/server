package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.UniqueViolationError;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.UniqueViolationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.exceptions.NoSuchEntityException;
import ar.edu.itba.iot.carne_iot.server.exceptions.UnauthorizedException;
import ar.edu.itba.iot.carne_iot.server.models.Role;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.UserQueryHelper;
import ar.edu.itba.iot.carne_iot.server.security.PasswordValidator;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

/**
 * Concrete implementation of {@link UserService}.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UniqueViolationExceptionThrower {

    /**
     * DAO for managing {@link User}s data.
     */
    private final UserDao userDao;

    /**
     * Object in charge of creating {@link org.springframework.data.jpa.domain.Specification} of {@link User}s.
     */
    private final UserQueryHelper userQueryHelper;

    /**
     * {@link PasswordValidator} used to check whether a password is valid.
     */
    private final PasswordValidator passwordValidator;

    /**
     * {@link PasswordEncoder} used for hashing passwords when creating a new {@link User}.
     */
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserQueryHelper userQueryHelper,
                           PasswordValidator passwordValidator, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.userQueryHelper = userQueryHelper;
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @PreAuthorize("@userPermissionProvider.isAdmin()")
    public Page<User> findMatching(String fullName, LocalDate minBirthDate, LocalDate maxBirthDate,
                                   String username, String email, Pageable pageable) {
        userQueryHelper.validatePageable(pageable);
        final Specification<User> matching = userQueryHelper
                .createUserSpecification(fullName, minBirthDate, maxBirthDate, username, email);

        return userDao.findAll(matching, pageable);
    }

    @Override
    @PreAuthorize("@userPermissionProvider.readById(#id)")
    public Optional<User> getById(long id) {
        return getInitializing(userDao::findById, id);
    }

    @Override
    @PreAuthorize("@userPermissionProvider.readByUsername(#username)")
    public Optional<User> getByUsername(String username) {
        return getInitializing(userDao::findByUsername, username);
    }

    @Override
    @PreAuthorize("@userPermissionProvider.readByEmail(#email)")
    public Optional<User> getByEmail(String email) {
        return getInitializing(userDao::findByEmail, email);
    }


    @Override
    @Transactional
    public User register(String fullName, LocalDate birthDate, String username, String email, String password) {
        final List<UniqueViolationError> errorList = new LinkedList<>();
        checkUsernameUniqueness(username, errorList);
        checkEmailUniqueness(email, errorList);
        throwUniqueViolationException(errorList);

        passwordValidator.validate(password);
        // Just in case validate did not check the password is null...
        Objects.requireNonNull(password, "The password must not be null");
        final User user = new User(fullName, birthDate, username, email, passwordEncoder.encode(password));
        return userDao.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.writeById(#id)")
    public void update(long id, String newFullName, LocalDate newBirthDate) {
        final User user = userDao.findById(id).orElseThrow(NoSuchEntityException::new);
        user.update(newFullName, newBirthDate);
        userDao.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.writeById(#id)")
    public void changeUsername(long id, String newUsername) {
        final List<UniqueViolationError> errorList = new LinkedList<>();
        checkUsernameUniqueness(newUsername, errorList);
        throwUniqueViolationException(errorList);

        final User user = userDao.findById(id).orElseThrow(NoSuchEntityException::new);
        user.changeUsername(newUsername);
        userDao.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.writeById(#id)")
    public void changeEmail(long id, String newEmail) {
        final List<UniqueViolationError> errorList = new LinkedList<>();
        checkEmailUniqueness(newEmail, errorList);
        throwUniqueViolationException(errorList);

        final User user = userDao.findById(id).orElseThrow(NoSuchEntityException::new);
        user.changeEmail(newEmail);
        userDao.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.writeById(#id)")
    public void changePassword(long id, String currentPassword, String newPassword) {
        final User user = userDao.findById(id).orElseThrow(NoSuchEntityException::new);

        // If currentPassword doesn't match with the actual password, do not change it
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getHashedPassword())) {
            throw new UnauthorizedException("The given current password did not match the user's password");
        }
        passwordValidator.validate(newPassword);
        // Just in case validate did not check the password is null...
        Objects.requireNonNull(newPassword, "The new password must not be null");
        user.changePassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }

    @Override
    @PreAuthorize("@userPermissionProvider.isAdmin()")
    public Set<Role> getRoles(long id) {
        return getInitializing(userDao::findById, id).map(User::getRoles).orElseThrow(NoSuchEntityException::new);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.isAdmin()")
    public void addRole(long id, Role role) {
        final User user = userDao.findById(id).orElseThrow(NoSuchEntityException::new);
        user.addRole(role);
        userDao.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.isAdmin()")
    public void removeRole(long id, Role role) {
        final User user = userDao.findById(id).orElseThrow(NoSuchEntityException::new);
        user.removeRole(role);
        userDao.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.deleteById(#id)")
    public void deleteById(long id) {
        userDao.findById(id).ifPresent(userDao::delete);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.deleteByUsername(#username)")
    public void deleteByUsername(String username) {
        userDao.findByUsername(username).ifPresent(userDao::delete);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.deleteByEmail(#email)")
    public void deleteByEmail(String email) {
        userDao.findByEmail(email).ifPresent(userDao::delete);
    }


    // ================================
    // Helpers
    // ================================

    /**
     * Checks that no {@link User} exists with the given {@code email},
     * adding the {@link UserServiceImpl#EMAIL_IN_USE} {@link UniqueViolationError} to the given {@code errorList}
     * if it exists.
     *
     * @param email     The email that must be unique.
     * @param errorList A {@link List} of {@link UniqueViolationError}
     *                  that might have occurred before executing the method.
     */
    private void checkEmailUniqueness(String email, List<UniqueViolationError> errorList) {
        if (userDao.existsByEmail(email)) {
            errorList.add(EMAIL_IN_USE);
        }
    }

    /**
     * @param username  The username that must be unique.
     * @param errorList A {@link List} of {@link UniqueViolationError}
     *                  that might have occurred before executing the method.
     */
    private void checkUsernameUniqueness(String username, List<UniqueViolationError> errorList) {
        if (userDao.existsByUsername(username)) {
            errorList.add(USERNAME_IN_USE);
        }
    }

    /**
     * Retrieves a {@link User} {@link Optional} using the given {@code searchFunction}, and the given {@code criteria}.
     * Will initialize all LAZY relationships of the retrieved {@link User}.
     * In case no {@link User} was found, the {@link Optional} will be empty.
     *
     * @param searchFunction The {@link Function} to be used to search.
     * @param criteria       The criteria used to find the user (i.e the input for the given {@code searchFunction}.
     * @param <T>            The specific type of the given {@code criteria}.
     * @return A nullable {@link Optional} containing the matching {@link User}.
     */
    private static <T> Optional<User> getInitializing(Function<T, Optional<User>> searchFunction, T criteria) {
        final Optional<User> user = searchFunction.apply(criteria);
        // Initializes LAZY relationships
        user.map(User::getRoles).ifPresent(Hibernate::initialize);

        return user;
    }

    private static final UniqueViolationError USERNAME_IN_USE =
            new UniqueViolationError("The username is already in use", "username");

    private static final UniqueViolationError EMAIL_IN_USE =
            new UniqueViolationError("The email is already in use", "email");
}
