package ar.edu.itba.iot.carne_iot.server.web;

import javax.ws.rs.core.Response;

/**
 * Class containing constants to be used by the web package.
 */
public class Constants {


    // ================================================================
    // Default values
    // ================================================================

    /**
     * The default page number (in string format).
     */
    public static final String DEFAULT_PAGE_NUMBER_STRING = "0";
    /**
     * The default page size (in string format).
     */
    public static final String DEFAULT_PAGE_SIZE_STRING = "25";
    /**
     * The default page number.
     */
    public static final Integer DEFAULT_PAGE_NUMBER = Integer.valueOf(DEFAULT_PAGE_NUMBER_STRING);
    /**
     * The default page size.
     */
    public static final int DEFAULT_PAGE_SIZE = Integer.valueOf(DEFAULT_PAGE_SIZE_STRING);


    // ================================================================
    // Minimum and Maximum values
    // ================================================================

    /**
     * The max. value a page can have.
     */
    public static final int MAX_PAGE_SIZE = 100;


    // ================================================================
    // Missing HTTP Statuses
    // ================================================================

    /**
     * Enum containing missing {@link javax.ws.rs.core.Response.StatusType}
     * not included in {@link javax.ws.rs.core.Response.Status} enum.
     */
    public enum MissingHttpStatuses implements Response.StatusType {
        UNPROCESSABLE_ENTITY(422, "Unprocessable Entity");

        /**
         * The associated status code.
         */
        private final int code;
        /**
         * The reason phrase.
         */
        private final String reason;
        /**
         * The class of status code.
         */
        private final Response.Status.Family family;


        /**
         * Constructor.
         *
         * @param statusCode   The associated status code.
         * @param reasonPhrase The reason phrase.
         */
        MissingHttpStatuses(final int statusCode, final String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
            this.family = Response.Status.Family.familyOf(statusCode);
        }

        /**
         * @return The class of status code.
         */
        @Override
        public Response.Status.Family getFamily() {
            return family;
        }

        /**
         * @return The associated status code.
         */
        @Override
        public int getStatusCode() {
            return code;
        }

        /**
         * @return The reason phrase.
         */
        @Override
        public String getReasonPhrase() {
            return this.toString();
        }

        /**
         * Get the reason phrase.
         *
         * @return the reason phrase.
         */
        @Override
        public String toString() {
            return reason;
        }

        /**
         * Convert a numerical status code into the corresponding Status.
         * Supports {@link Response.StatusType} declared in this enum,
         * and also declared in {@link Response.Status} enum.
         * (using {@link Response.StatusType} instead of {@link Response.Status}).
         *
         * @param statusCode The numerical status code.
         * @return The matching Status, or {@code null} is no matching Status is defined.
         */
        public static Response.StatusType fromStatusCode(final int statusCode) {
            for (Response.StatusType status : MissingHttpStatuses.values()) {
                if (status.getStatusCode() == statusCode) {
                    return status;
                }
            }
            return Response.Status.fromStatusCode(statusCode);
        }
    }
}
