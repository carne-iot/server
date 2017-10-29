package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Helps the process of applying base64url encoding and decoding.
 */
public class Base64UrlHelper {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Base64UrlException.class);


    /**
     * Performs base64url encoding of the given {@code rawNumber}.
     *
     * @param rawNumber                     The {@link Number} to encode.
     * @param toNotYetEncodedStringFunction {@link Function} from {@link String} to {@code T}
     *                                      that transforms a base64url decoded {@link String}
     *                                      into an object of type {@code T}.
     * @param <T>                           The concrete subtype of {@link Number}.
     * @return The encoded {@link Number}.
     */
    public static <T extends Number> String encodeFromNumber(T rawNumber,
                                                             Function<T, String> toNotYetEncodedStringFunction) {
        try {
            final String asString = toNotYetEncodedStringFunction.apply(rawNumber);

            return Base64Utils.encodeToUrlSafeString(asString.getBytes());
        } catch (Throwable e) {
            LOGGER.debug("{} value could not be encoded. Exception message: {}", rawNumber, e.getMessage());
            LOGGER.trace("Exception stacktrace", e);

            throw new Base64UrlException("Could not encode value " + rawNumber.toString(), e);
        }
    }

    /**
     * Performs decoding into a {@link Number}.
     *
     * @param encodedNumber             The {@link String} representing a {@link Number} in base64url format.
     * @param fromDecodedStringFunction {@link Function} from  {@code T} to {@link String}
     *                                  that transforms an object of type {@code T}
     *                                  into the {@link String} that will be encoded with base64url.
     * @param <T>                       The concrete subtype of {@link Number}.
     * @return The decoded {@link Number}.
     */
    public static <T extends Number> T decodeToNumber(String encodedNumber,
                                                      Function<String, T> fromDecodedStringFunction) {
        try {
            final byte[] bytes = Base64Utils.decodeFromUrlSafeString(encodedNumber);
            final String asString = new String(bytes, StandardCharsets.UTF_8);

            return fromDecodedStringFunction.apply(asString);
        } catch (IllegalArgumentException e) {
            LOGGER.debug("{} value could not be decoded. Exception message: {}", encodedNumber, e.getMessage());
            LOGGER.trace("Exception stacktrace", e);

            throw new Base64UrlException("Could not decode value " + encodedNumber, e);
        }
    }

    /**
     * Exception to be thrown when a base64url encoding or decoding operation fails.
     */
    private static final class Base64UrlException extends RuntimeException {

        /**
         * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
         * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
         *                For more information, see {@link RuntimeException#RuntimeException(Throwable)}.
         */
        private Base64UrlException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
