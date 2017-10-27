package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.exceptions.NoSuchEntityException;
import ar.edu.itba.iot.carne_iot.server.models.Session;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.SessionDao;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.SessionQueryHelper;
import ar.edu.itba.iot.carne_iot.server.security.CurrentUserIdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Concrete implementation of {@link SessionService}.
 */
@Service
@Transactional(readOnly = true)
public class SessionServiceImpl implements SessionService {

    /**
     * DAO for retrieving {@link Session}s data.
     */
    private final SessionDao sessionDao;

    /**
     * Object in charge of assisting the process of querying {@link Session}s.
     */
    private final SessionQueryHelper sessionQueryHelper;

    /**
     * {@link CurrentUserIdProvider} to know the currenly authenticated user id.
     */
    private final CurrentUserIdProvider currentUserIdProvider;

    /**
     * {@link UserDao} to retrieve
     */
    private final UserDao userDao;

    @Autowired
    public SessionServiceImpl(SessionDao sessionDao, UserDao userDao,
                              SessionQueryHelper sessionQueryHelper, CurrentUserIdProvider currentUserIdProvider) {
        this.sessionDao = sessionDao;
        this.sessionQueryHelper = sessionQueryHelper;
        this.currentUserIdProvider = currentUserIdProvider;
        this.userDao = userDao;
    }


    @Override
    @PreAuthorize("@userPermissionProvider.readById(#owner.id)")
    public Page<Session> listSessions(User owner, Pageable pageable) {
        sessionQueryHelper.validatePageable(pageable);
        return sessionDao.findByOwner(owner, pageable);
    }


    @Override
    public boolean validSession(long ownerId, long jti) {
        return sessionDao.findByOwnerIdAndJti(ownerId, jti).map(Session::isValid).orElse(false);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.writeById(#ownerId)")
    public void invalidateSession(long ownerId, long jti) {
        final Session session = sessionDao.findByOwnerIdAndJti(ownerId, jti)
                .orElseThrow(NoSuchEntityException::new);
        session.blacklist();
        sessionDao.save(session);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return currentUserIdProvider.currentUserIdOptional()
                .map(id -> userDao.findById(id).orElseThrow(NoSuchEntityException::new));

    }
}
