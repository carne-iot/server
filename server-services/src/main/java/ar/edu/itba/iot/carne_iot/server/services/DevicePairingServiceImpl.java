package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.exceptions.NoSuchEntityException;
import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.Session;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.DeviceDao;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.SessionDao;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao;
import ar.edu.itba.iot.carne_iot.server.security.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Concrete implementation of {@link DevicePairingService}.
 */
@Service
public class DevicePairingServiceImpl implements DevicePairingService {

    /**
     * Amount of tries to perform the pairing process.
     */
    private static final int MAX_TRIES = 10;

    /**
     * A {@link JwtTokenGenerator} to create tokens when login is performed.
     */
    private final JwtTokenGenerator jwtTokenGenerator;

    /**
     * DAO for retrieving {@link User}s data.
     */
    private final UserDao userDao;

    /**
     * DAO for retrieving {@link Device}s data.
     */
    private final DeviceDao deviceDao;

    /**
     * DAO for retrieving {@link Session}s data.
     */
    private final SessionDao sessionDao;


    @Autowired
    public DevicePairingServiceImpl(JwtTokenGenerator jwtTokenGenerator,
                                    UserDao userDao, DeviceDao deviceDao, SessionDao sessionDao) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.sessionDao = sessionDao;
    }


    @Override
    public String pair(long ownerId, long deviceId) {
        final User user = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);

        // Try to create the token...
        boolean validToken = false;
        int tries = 0;
        JwtTokenGenerator.TokenAndSessionContainer container = null;
        while (!validToken && tries < MAX_TRIES) {
            container = jwtTokenGenerator.generateDeviceToken(user, device);
            validToken = !sessionDao.existsByOwnerAndJti(user, container.getJti());
            tries++;
        }
        if (tries >= MAX_TRIES) {
            throw new RuntimeException("Could not create a session after " + MAX_TRIES + "tries");
        }

        // Store token in order to pass the
        Objects.requireNonNull(container, "The container was not initialized correctly");
        final Session session = new Session(user, container.getJti());  // Actually not a session, but whatever
        sessionDao.save(session);

        return container.getToken();
    }
}
