package ar.edu.itba.iot.carne_iot.server.services;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.IllegalStateError;
import ar.edu.itba.iot.carne_iot.server.error_handling.errros.UniqueViolationError;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.UniqueViolationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.exceptions.CustomIllegalStateException;
import ar.edu.itba.iot.carne_iot.server.exceptions.NoSuchEntityException;
import ar.edu.itba.iot.carne_iot.server.exceptions.UniqueViolationException;
import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.DeviceDao;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.DeviceRegistrationDao;
import ar.edu.itba.iot.carne_iot.server.persistence.daos.UserDao;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.DeviceQueryHelper;
import ar.edu.itba.iot.carne_iot.server.persistence.query_helpers.DeviceRegistrationQueryHelper;
import ar.edu.itba.iot.carne_iot.server.security.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Concrete implementation of {@link DeviceService}.
 */
@Service
@Transactional(readOnly = true)
public class DeviceServiceImpl implements DeviceService, UniqueViolationExceptionThrower {

    /**
     * A system {@link User} to be used to create device tokens.
     */
    private static final User deviceUser = new User("device",
            LocalDate.of(1900, 1, 1),
            "username", "email@email.com", "hashed");

    /**
     * DAO for managing {@link User}s data.
     */
    private final UserDao userDao;

    /**
     * DAO for managing {@link Device}s data.
     */
    private final DeviceDao deviceDao;

    /**
     * DAO for managing {@link ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration}s data.
     */
    private final DeviceRegistrationDao deviceRegistrationDao;

    /**
     * Object in charge of assisting {@link Device} queries.
     */
    private final DeviceQueryHelper deviceQueryHelper;

    /**
     * Object in charge of assisting {@link ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration} queries.
     */
    private final DeviceRegistrationQueryHelper deviceRegistrationQueryHelper;

    /**
     * A {@link JwtTokenGenerator} to create tokens when login is performed.
     */
    private final JwtTokenGenerator jwtTokenGenerator;


    @Autowired
    public DeviceServiceImpl(UserDao userDao, DeviceDao deviceDao, DeviceRegistrationDao deviceRegistrationDao,
                             DeviceQueryHelper deviceQueryHelper,
                             DeviceRegistrationQueryHelper deviceRegistrationQueryHelper,
                             JwtTokenGenerator jwtTokenGenerator) {
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.deviceRegistrationDao = deviceRegistrationDao;
        this.deviceQueryHelper = deviceQueryHelper;
        this.deviceRegistrationQueryHelper = deviceRegistrationQueryHelper;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }


    @Override
    @PreAuthorize("@adminPermissionProvider.isAdmin()")
    public Page<RegisteredDeviceWrapper> listDevices(Pageable pageable) {
        deviceQueryHelper.validatePageable(pageable);

        final Page<Device> devices = deviceDao.findAll(pageable);
        return devices.map(device -> toRegisteredDeviceWrapper().apply(device));
    }

    @Override
    @PreAuthorize("@adminPermissionProvider.isAdmin()")
    public Optional<RegisteredDeviceWrapper> getDeviceWithRegistrationData(long deviceId) {
        return deviceDao.findById(deviceId).map(toRegisteredDeviceWrapper());
    }

    @Override
    @Transactional
    @PreAuthorize("@adminPermissionProvider.isAdmin()")
    public Device createDevice(long deviceId) {
        if (deviceDao.exists(deviceId)) {
            throwUniqueViolationException(Collections.singletonList(DEVICE_ALREADY_CREATED));
        }

        final Device device = new Device(deviceId);
        deviceDao.save(device);
        return device;
    }

    @Override
    @PreAuthorize("@userPermissionProvider.readById(#ownerId)")
    public Page<DeviceWithNicknameWrapper> listUserDevices(long ownerId, Pageable pageable) {
        final User owner = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);

        deviceRegistrationQueryHelper.validatePageable(pageable);
        final Pageable adaptedPageable = Optional.ofNullable(pageable.getSort())
                .map(deviceRegistrationQueryHelper::adaptSort)
                .map(sort -> (Pageable) new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort))
                .orElse(pageable);

        final Specification<DeviceRegistration> matching = deviceRegistrationQueryHelper
                .createDeviceSpecification(owner);

        return deviceRegistrationDao.findAll(matching, adaptedPageable).map(DeviceWithNicknameWrapper::new);
    }

    @Override
    @PreAuthorize("@devicePermissionProvider.isOwnerOrAdmin(#deviceId)")
    public Optional<DeviceWithNicknameWrapper> getRegisteredDevice(long ownerId, long deviceId) {
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);
        final User owner = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);

        return deviceRegistrationDao.findByDeviceAndOwnerAndActiveTrue(device, owner)
                .map(DeviceWithNicknameWrapper::new);
    }

    @Override
    @Transactional
    @PreAuthorize("@devicePermissionProvider.isOwnerOrAdmin(#deviceId)")
    public void setNickname(long ownerId, long deviceId, String nickname) {
        performRegistrationChangeOfState(ownerId, deviceId, registration -> {
            if (nickname != null
                    && !nickname.equals(registration.getNickname())
                    && deviceRegistrationDao.existsByOwnerAndNickname(registration.getOwner(), nickname)) {
                throw new UniqueViolationException(Collections.singletonList(NICKNAME_ALREADY_IN_USE));
            }
            registration.setNickname(nickname);
        });
    }

    @Override
    @Transactional
    @PreAuthorize("@devicePermissionProvider.isOwnerOrAdmin(#deviceId)")
    public void deleteNickname(long ownerId, long deviceId) {
        performRegistrationChangeOfState(ownerId, deviceId, DeviceRegistration::removeNickname);
    }

    @Override
    @Transactional
    @PreAuthorize("@userPermissionProvider.writeById(#ownerId)")
    public void registerDevice(long ownerId, long deviceId) {
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);
        final User user = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);

        // If the device is already registered by the user, return and be idempotent
        if (deviceRegistrationDao.existsByDeviceAndOwnerAndActiveTrue(device, user)) {
            return;
        }

        // Check if device is not registered already
        if (deviceRegistrationDao.existsByDeviceAndActiveTrue(device)) {
            throwUniqueViolationException(Collections.singletonList(ALREADY_REGISTERED));
        }

        final DeviceRegistration deviceRegistration = new DeviceRegistration(device, user);
        deviceRegistrationDao.save(deviceRegistration);
    }

    @Override
    @Transactional
    @PreAuthorize("@devicePermissionProvider.isOwnerOrAdmin(#deviceId)")
    public void unregisterDevice(long deviceId) {
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);

        // Finds the registration for the device to the given user.
        // If present, it unregisters, else it does not do anything (i.e is idempotent).
        deviceRegistrationDao.findByDeviceAndActiveTrue(device)
                .ifPresent(reg -> {
                    reg.inactivate();
                    deviceRegistrationDao.save(reg);
                });
    }

    @Override
    @PreAuthorize("@devicePermissionProvider.isOwnerOrAdmin(#deviceId)")
    public String pair(long ownerId, long deviceId) {
        final User user = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);

        // Verify the device is registered
        if (!deviceRegistrationDao.existsByDeviceAndActiveTrue(device)) {
            throw new CustomIllegalStateException(OPERATION_OVER_UNREGISTERED_DEVICE);
        }

        return jwtTokenGenerator.generateDeviceToken(user, device).getToken();
    }

    @Override
    @PreAuthorize("@adminPermissionProvider.isAdmin()")
    public String pair(long deviceId) {
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);
        return jwtTokenGenerator.generateDeviceToken(deviceUser, device).getToken();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole(T(ar.edu.itba.iot.carne_iot.server.models.Role).ROLE_DEVICE) " +
            "and @devicePermissionProvider.isOwnDevice(#deviceId)")
    public void updateTemperature(long deviceId, BigDecimal temperature) {
        performChangeOfState(deviceId, (deviceLambda) -> deviceLambda.setTemperature(temperature));
    }

    @Override
    @Transactional
    @PreAuthorize("@devicePermissionProvider.isOwnerOrAdmin(#deviceId)")
    public void setTargetTemperature(long deviceId, BigDecimal targetTemperature) {
        performChangeOfState(deviceId, device -> device.setTargetTemperature(targetTemperature));
    }

    @Override
    @Transactional
    @PreAuthorize("@devicePermissionProvider.isOwnerOrAdmin(#deviceId)")
    public void clearTargetTemperature(long deviceId) {
        performChangeOfState(deviceId, Device::removeTargetTemperature);
    }

    // ================================
    // Helpers
    // ================================

    /**
     * Returns a {@link Function} that takes a {@link Device}
     * and creates a {@link DeviceService.RegisteredDeviceWrapper} according to the {@link Device} it takes.
     *
     * @return The said function.
     */
    private Function<Device, ? extends RegisteredDeviceWrapper> toRegisteredDeviceWrapper() {
        return (device) -> deviceRegistrationDao.findByDeviceAndActiveTrue(device)
                .map(reg -> new RegisteredDeviceWrapper(reg.getDevice(), reg.getOwner()))
                .orElse(new RegisteredDeviceWrapper(device));
    }

    /**
     * Performs a change of state operation, saving the {@link Device} with the given {@code deviceId}.
     *
     * @param deviceId             The id of the {@link Device} to which the operation will be performed.
     * @param changeStateOperation The change of state operation (represented as a {@link Consumer}).
     */
    private void performChangeOfState(long deviceId, Consumer<Device> changeStateOperation) {
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);

        // Check if the device is registered
        if (!deviceRegistrationDao.existsByDeviceAndActiveTrue(device)) {
            throw new CustomIllegalStateException(OPERATION_OVER_UNREGISTERED_DEVICE);
        }

        changeStateOperation.accept(device);
        deviceDao.save(device);
    }

    /**
     * Performs a change of state operation, saving the {@link Device} with the given {@code deviceId}.
     *
     * @param ownerId              The id of the {@link User} owning the {@link Device}
     *                             to which the operation will be performed.
     * @param deviceId             The id of the {@link Device} to which the operation will be performed.
     * @param changeStateOperation The change of state operation (represented as a {@link Consumer}).
     */
    private void performRegistrationChangeOfState(long ownerId, long deviceId,
                                                  Consumer<DeviceRegistration> changeStateOperation) {
        final Device device = deviceDao.findById(deviceId).orElseThrow(NoSuchEntityException::new);
        final User user = userDao.findById(ownerId).orElseThrow(NoSuchEntityException::new);

        final DeviceRegistration registration = deviceRegistrationDao.findByDeviceAndOwnerAndActiveTrue(device, user)
                .orElseThrow(NoSuchEntityException::new);

        changeStateOperation.accept(registration);
        deviceRegistrationDao.save(registration);
    }

    private static final UniqueViolationError DEVICE_ALREADY_CREATED =
            new UniqueViolationError("The device id is already created", "deviceId");

    private static final UniqueViolationError ALREADY_REGISTERED =
            new UniqueViolationError("The device id is already registered", "deviceId");

    private static final UniqueViolationError NICKNAME_ALREADY_IN_USE =
            new UniqueViolationError("The nickname for the device is already in use",
                    "nickname", "userId");

    private static final IllegalStateError OPERATION_OVER_UNREGISTERED_DEVICE =
            new IllegalStateError("Device must be registered to operate over it",
                    Device.class.getSimpleName());
}
