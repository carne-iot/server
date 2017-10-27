package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.jersey_providers;

import ar.edu.itba.iot.carne_iot.server.web.support.annotations.Base64url;
import org.springframework.util.Base64Utils;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link ParamConverterProvider} for url safe base64 encoded params.
 */
@Provider
public class UrlSafeBase64DecodedParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        final Map<Class<? extends Annotation>, Annotation> annotationsMap =
                Arrays.stream(annotations).collect(Collectors.toMap(Annotation::annotationType, Function.identity()));

        if (annotationsMap.containsKey(Base64url.class)) {

            if (rawType == Long.class) {
                //noinspection unchecked
                return (ParamConverter<T>) new UrlSafeBase64EncodedParamConverter<>(Long::valueOf, Object::toString);
            }
            if (rawType == Integer.class) {
                //noinspection unchecked
                return (ParamConverter<T>) new UrlSafeBase64EncodedParamConverter<>(Integer::valueOf, Object::toString);
            }
            if (rawType == Short.class) {
                //noinspection unchecked
                return (ParamConverter<T>) new UrlSafeBase64EncodedParamConverter<>(Short::valueOf, Object::toString);
            }
            if (rawType == Byte.class) {
                //noinspection unchecked
                return (ParamConverter<T>) new UrlSafeBase64EncodedParamConverter<>(Byte::valueOf, Object::toString);
            }
        }
        return null;
    }

    /**
     * A {@link ParamConverter} from a url safe base 64 encoded value into any subclass of {@link Number},
     *
     * @param <T> The concrete subclass of {@link Number}.
     */
    private static final class UrlSafeBase64EncodedParamConverter<T extends Number>
            implements ParamConverter<T> {


        /**
         * {@link Function} from {@link String} to {@code T} that transforms a base64url decoded {@link String}
         * into an object of type {@code T}.
         */
        private final Function<String, T> fromDecodedStringFunction;

        /**
         * {@link Function} from  {@code T} to {@link String} that transforms an object of type {@code T}
         * into the {@link String} that will be encoded with base64url.
         */
        private final Function<T, String> toNotYetEncodedStringFunction;

        /**
         * Constructor.
         *
         * @param fromDecodedStringFunction     {@link Function} from {@link String} to {@code T}
         *                                      that transforms a base64url decoded {@link String}
         *                                      into an object of type {@code T}.
         * @param toNotYetEncodedStringFunction {@link Function} from  {@code T} to {@link String}
         *                                      that transforms an object of type {@code T}
         *                                      into the {@link String} that will be encoded with base64url.
         */
        private UrlSafeBase64EncodedParamConverter(Function<String, T> fromDecodedStringFunction,
                                                   Function<T, String> toNotYetEncodedStringFunction) {
            this.fromDecodedStringFunction = fromDecodedStringFunction;
            this.toNotYetEncodedStringFunction = toNotYetEncodedStringFunction;
        }

        @Override
        public T fromString(String value) {
            final byte[] bytes = Base64Utils.decodeFromUrlSafeString(value);
            final String asString = new String(bytes, StandardCharsets.UTF_8);
            return fromDecodedStringFunction.apply(asString);
        }

        @Override
        public String toString(T value) {
            final String asString = toNotYetEncodedStringFunction.apply(value);

            return Base64Utils.encodeToUrlSafeString(asString.getBytes());
        }
    }
}
