package ar.edu.itba.iot.carne_iot.server.models;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationHelper;
import ar.edu.itba.iot.carne_iot.server.exceptions.ValidationException;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationConstants;
import ar.edu.itba.iot.carne_iot.server.models.constants.ValidationErrorConstants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a {@link User}'s food preference.
 */
@Entity
@Table(name = "food_preferences", indexes = {
        @Index(name = "food_preferences_name_user_id_unique_index", columnList = "name, user_id", unique = true),
})
public class FoodPreference implements ValidationExceptionThrower {

    /**
     * The preference id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The preference name.
     */
    @Column(name = "name")
    private String name;

    /**
     * The preference target temperature.
     */
    @Column(name = "temperature", precision = Device.PRECISION, scale = Device.SCALE)
    private BigDecimal temperature;

    /**
     * The {@link User} owning this preference.
     */
    @JoinColumn(columnDefinition = "integer", name = "user_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User owner;


    /* package */ FoodPreference() {
        // For Hibernate
    }

    /**
     * Constructor.
     *
     * @param name        The preference name.
     * @param temperature The preference target temperature.
     * @param owner       The {@link User} owning this preference.
     * @throws ValidationException If any of the given arguments is not valid.
     */
    public FoodPreference(String name, BigDecimal temperature, User owner) throws ValidationException {
        final List<ValidationError> errorList = new LinkedList<>();
        if (owner == null) {
            errorList.add(ValidationErrorConstants.MISSING_OWNER);
        }
        validate(name, temperature, errorList);
        throwValidationException(errorList);

        this.name = name;
        this.temperature = temperature;
        this.owner = owner;
    }

    /**
     * Updates this preference.
     *
     * @param name        The new name.
     * @param temperature The new temperature.
     * @throws ValidationException If any of the given arguments is not valid.
     */
    public void update(String name, BigDecimal temperature) throws ValidationException {
        validate(name, temperature);

        this.name = name;
        this.temperature = temperature;
    }

    /**
     * @return The preference id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The preference name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The preference target temperature.
     */
    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * @return The {@link User} owning this preference.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Performs validation over the given arguments.
     *
     * @param name        The name to be validated.
     * @param temperature The temperature to be validated.
     * @throws ValidationException If any of the given arguments is not valid.
     */
    private void validate(String name, BigDecimal temperature) throws ValidationException {
        final List<ValidationError> errorList = new LinkedList<>();
        validate(name, temperature, errorList);
        throwValidationException(errorList);
    }

    /**
     * Performs validation over the given arguments, adding detected errors in the given {@code errorList}.
     *
     * @param name        The name to be validated.
     * @param temperature The temperature to be validated.
     * @param errorList   A {@link List} of {@link ValidationError} where detected errors are stored.
     */
    private void validate(String name, BigDecimal temperature, List<ValidationError> errorList) {
        Objects.requireNonNull(errorList, "The error list must not be null");

        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(name, ValidationConstants.PREFERENCE_NAME_MIN_LENGTH,
                ValidationConstants.PREFERENCE_NAME_MAX_LENGTH, errorList,
                ValidationErrorConstants.MISSING_PREFERENCE_NAME,
                ValidationErrorConstants.PREFERENCE_NAME_TOO_SHORT, ValidationErrorConstants.PREFERENCE_NAME_TOO_LONG);
        ValidationHelper.objectNotNull(temperature, errorList, ValidationErrorConstants.MISSING_TEMPERATURE);
        if (temperature.compareTo(ValidationConstants.MIN_TEMPERATURE) < 0) {
            errorList.add(ValidationErrorConstants.TOO_LOW_TEMPERATURE);
        } else if (temperature.compareTo(ValidationConstants.MAX_TEMPERATURE) > 0) {
            errorList.add(ValidationErrorConstants.TOO_HIGH_TEMPERATURE);
        }
    }
}
