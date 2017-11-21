package ar.edu.itba.iot.carne_iot.server.models;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationHelper;
import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationConstants;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationErrorConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
     * Sets the actual temperature measured by this device.
     *
     * @param temperature The actual temperature measured by this device.
     * @throws ValidationException If the temperature is not valid.
     */
    public void setTemperature(BigDecimal temperature) throws ValidationException {
        validateTemperature(temperature);

        this.temperature = temperature;
        this.lastTemperatureUpdate = Instant.now();
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

    /* package */ static final int PRECISION = 5;
    /* package */ static final int SCALE = 2;
}
