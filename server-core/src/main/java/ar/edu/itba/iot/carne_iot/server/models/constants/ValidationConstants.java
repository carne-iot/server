package ar.edu.itba.iot.carne_iot.server.models.constants;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Constants to be used when validating entities.
 */
public class ValidationConstants {

    // ==================================
    // Private stuff
    // ==================================

    private final static String MIN_TEMPERATURE_STRING = "-999.99";
    private final static String MAX_TEMPERATURE_STRING = "999.99";

    // ==================================
    // Minimum values
    // ==================================

    public final static int NAME_MIN_LENGTH = 1;
    public final static LocalDate MIN_BIRTH_DATE = LocalDate.MIN;
    public final static int USERNAME_MIN_LENGTH = 4;
    public final static int EMAIL_MIN_LENGTH = 4;
    /**
     * The minimum age a {@link ar.edu.itba.iot.carne_iot.server.models.User} can have.
     */
    public static final int MINIMUM_AGE = 13;
    public final static BigDecimal MIN_TEMPERATURE = new BigDecimal(MIN_TEMPERATURE_STRING);
    public final static int NICKNAME_MIN_LENGTH = 1;
    public final static int PREFERENCE_NAME_MIN_LENGTH = 1;


    // ==================================
    // Maximum values
    // ==================================

    public final static int NAME_MAX_LENGTH = 64;
    public final static int USERNAME_MAX_LENGTH = 64;
    public final static int EMAIL_MAX_LENGTH = 254;
    public final static BigDecimal MAX_TEMPERATURE = new BigDecimal(MAX_TEMPERATURE_STRING);
    public final static int NICKNAME_MAX_LENGTH = 256;
    public final static int PREFERENCE_NAME_MAX_LENGTH = 256;

}
