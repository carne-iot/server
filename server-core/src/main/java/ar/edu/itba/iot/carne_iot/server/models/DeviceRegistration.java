package ar.edu.itba.iot.carne_iot.server.models;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationHelper;
import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationConstants;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationErrorConstants;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a device registration (i.e registration data).
 */
@Entity
@Table(name = "device_registrations",
        indexes = {
                @Index(name = "device_registrations_user_id_nickname_unique_index", columnList = "user_id, nickname")
        })
public class DeviceRegistration implements ValidationExceptionThrower {

    /**
     * The registration id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    /**
     * The device being registered.
     */
    @JoinColumn(columnDefinition = "integer", name = "device_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Device device;

    /**
     * The {@link User} that registered the {@link #device}.
     */
    @JoinColumn(columnDefinition = "integer", name = "user_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User owner;

    /**
     * The nickname given to this device by its {@link #owner}.
     */
    @Column(name = "nickname", length = ValidationConstants.NICKNAME_MAX_LENGTH)
    private String nickname;

    /**
     * Indicates the moment in which this registration was made.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Indicates whether this registration is active.
     */
    @Column(name = "active", nullable = false)
    private boolean active;


    /* package */ DeviceRegistration() {
        // For Hibernate
    }

    /**
     * Constructor.
     *
     * @param device The {@link Device} being registered.
     * @param owner  The {@link User} registering the {@code device}.
     * @throws ValidationException If the {@link Device} or the {@link User} are not valid.
     */
    public DeviceRegistration(Device device, User owner) throws ValidationException {
        validateCreation(device, owner);

        this.device = device;
        this.owner = owner;
        this.nickname = null;
        this.createdAt = Instant.now();
        this.active = true;
    }


    /**
     * @return The registration id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The device being registered.
     */
    public Device getDevice() {
        return device;
    }

    /**
     * @return The {@link User} that registered the {@link #device}.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @return The nickname given to this device by its {@link #owner}.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return Indicates the moment in which this registration was made.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * @return Indicates whether this registration is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Changes the nickname given to the registered device.
     *
     * @param nickname The new nickname.
     * @throws ValidationException If the nickname is not valid.
     */
    public void setNickname(String nickname) throws ValidationException {
        validateNickname(nickname);

        this.nickname = nickname;
    }


    // ====================
    // Validators
    // ====================

    /**
     * Checks that the creation params (i.e {@code device} and {@code owner}) are valid.
     *
     * @param device The {@link Device} to be validated.
     * @param owner  The {@link User} to be validated.
     * @throws ValidationException If any of both are not valid.
     */
    private void validateCreation(Device device, User owner) throws ValidationException {
        final List<ValidationError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(device, errorList, ValidationErrorConstants.MISSING_DEVICE);
        ValidationHelper.objectNotNull(owner, errorList, ValidationErrorConstants.MISSING_OWNER);

        throwValidationException(errorList);
    }

    /**
     * Checks that the given {@code nickname} is valid (i.e not null and between valid ranges).
     *
     * @param nickname The nickname to be validated.
     * @throws ValidationException If the nickname is not valid.
     */
    private void validateNickname(String nickname) throws ValidationException {
        final List<ValidationError> errorList = new LinkedList<>();
        ValidationHelper.stringNullOrLengthBetweenTwoValues(nickname,
                ValidationConstants.NICKNAME_MIN_LENGTH, ValidationConstants.NICKNAME_MAX_LENGTH,
                errorList, ValidationErrorConstants.NICKNAME_TOO_SHORT, ValidationErrorConstants.NICKNAME_TOO_LONG);

        throwValidationException(errorList);
    }
}
