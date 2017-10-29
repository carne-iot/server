package ar.edu.itba.iot.carne_iot.server.models;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.IllegalStateError;
import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationHelper;
import ar.edu.itba.iot.carne_iot.server.exceptions.CustomIllegalStateException;
import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationConstants;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationErrorConstants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a device (i.e a thermometer).
 */
@Entity
@Table(name = "devices")
public class Device implements ValidationExceptionThrower {

    /**
     * The device id.
     */
    @Id
    @Column(name = "id", updatable = false)
    private long id;

    /**
     * The last temperature measured (and informed) by this device.
     */
    @Column(name = "temperature", precision = PRECISION, scale = SCALE)
    private BigDecimal temperature;

    /**
     * The moment in which the actual temperature was set.
     */
    @Column(name = "last_temperature_update")
    private Instant lastTemperatureUpdate;

    /**
     * The actual state of this device.
     */
    @Column(name = "state", length = 64)
    @Enumerated(EnumType.STRING)
    private State state;


    /* package */ Device() {
        // For Hibernate
    }

    /**
     * Constructor.
     *
     * @param id The device id.
     */
    public Device(long id) {
        this.id = id;
        this.temperature = null;
        this.lastTemperatureUpdate = null;
        this.state = State.IDLE;
    }

    /**
     * @return The device id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The last temperature measured (and informed) by this device,
     * or {@code null} if the device did not measure any temperature yet.
     */
    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * @return The moment in which the actual temperature was set.
     */
    public Instant getLastTemperatureUpdate() {
        return lastTemperatureUpdate;
    }

    /**
     * @return The actual state of this device.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the actual temperature measured by this device.
     *
     * @param temperature The actual temperature measured by this device.
     * @throws ValidationException If the temperature is not valid.
     */
    public void setTemperature(BigDecimal temperature) throws ValidationException {
        if (this.state != State.ACTIVE) {
            throw new CustomIllegalStateException(CHANGE_TEMPERATURE_IN_NOT_ACTIVE_STATE);
        }
        validateTemperature(temperature);

        this.temperature = temperature;
        this.lastTemperatureUpdate = Instant.now();
    }

    /**
     * Changes this device state to active.
     */
    public void startCooking() {
        this.state = State.ACTIVE;
    }

    /**
     * Changes this device state to idle.
     */
    public void stopCooking() {
        this.state = State.IDLE;
    }


    /**
     * Enum listing all possible states for a {@link Device}.
     */
    public enum State {
        IDLE,
        ACTIVE,
    }


    // ====================
    // Validators
    // ====================

    /**
     * Checks that the given {@code temperature} is valid (i.e not null and between valid ranges).
     *
     * @param temperature The temperature to be validated.
     * @throws ValidationException If the temperature is not valid.
     */
    private void validateTemperature(BigDecimal temperature) throws ValidationException {
        final List<ValidationError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(temperature, errorList, ValidationErrorConstants.MISSING_TEMPERATURE);
        if (temperature.compareTo(ValidationConstants.MIN_TEMPERATURE) < 0) {
            errorList.add(ValidationErrorConstants.TOO_LOW_TEMPERATURE);
        } else if (temperature.compareTo(ValidationConstants.MAX_TEMPERATURE) > 0) {
            errorList.add(ValidationErrorConstants.TOO_HIGH_TEMPERATURE);
        }

        throwValidationException(errorList);
    }


    // ====================
    // Constants
    // ====================

    private static final int PRECISION = 5;
    private static final int SCALE = 2;

    private static final IllegalStateError CHANGE_TEMPERATURE_IN_NOT_ACTIVE_STATE =
            new IllegalStateError("Device must be active to set the temperature",
                    Device.class.getSimpleName());
}
