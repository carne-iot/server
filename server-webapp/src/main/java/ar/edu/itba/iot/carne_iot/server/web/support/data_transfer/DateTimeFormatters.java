package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer;

import java.time.format.DateTimeFormatter;

/**
 * Enum containing {@link DateTimeFormatter}s to be reused.
 */
public enum DateTimeFormatters {

    ISO_LOCAL_DATE(DateTimeFormatter.ISO_LOCAL_DATE),
    ISO_LOCAL_TIME(DateTimeFormatter.ISO_LOCAL_TIME),
    ISO_LOCAL_DATE_TIME(DateTimeFormatter.ISO_LOCAL_DATE_TIME);


    /**
     * Holds the {@link DateTimeFormatter} for the enum value.
     */
    private final DateTimeFormatter formatter;

    /**
     * Constructor.
     *
     * @param pattern The pattern to build the {@link DateTimeFormatter}.
     */
    DateTimeFormatters(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * Constructor.
     *
     * @param formatter The {@link DateTimeFormatter}.
     */
    DateTimeFormatters(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * @return The {@link DateTimeFormatter} for the enum value.
     */
    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
