package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.jersey_providers;

import ar.edu.itba.iot.carne_iot.server.web.support.annotations.Java8Time;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link ParamConverterProvider} for Java 8 time types.
 */
@Provider
public class Java8TimeParamConverterProvider implements ParamConverterProvider {


    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        final Map<Class<? extends Annotation>, Annotation> annotationsMap =
                Arrays.stream(annotations).collect(Collectors.toMap(Annotation::annotationType, Function.identity()));

        if (annotationsMap.containsKey(Java8Time.class)) {
            final DateTimeFormatter formatter = ((Java8Time) annotationsMap.get(Java8Time.class))
                    .formatter()
                    .getFormatter();

            if (rawType == LocalDate.class) {
                //noinspection unchecked
                return (ParamConverter<T>) new Java8TimeParamConverter<>(formatter, LocalDate::from);
            }
            if (rawType == LocalTime.class) {
                //noinspection unchecked
                return (ParamConverter<T>) new Java8TimeParamConverter<>(formatter, LocalTime::from);
            }
            if (rawType == LocalDateTime.class) {
                //noinspection unchecked
                return (ParamConverter<T>) new Java8TimeParamConverter<>(formatter, LocalDateTime::from);
            }
        }
        return null;
    }


    /**
     * A {@link ParamConverter} for any implementation of {@link TemporalAccessor} that can be obtained
     * by a {@link Function} that receives a {@link TemporalAccessor}.
     *
     * @param <E> The type that can be obtained by applying a {@link Function} to a {@link TemporalAccessor}.
     */
    private static final class Java8TimeParamConverter<E extends TemporalAccessor> implements ParamConverter<E> {

        /**
         * The {@link DateTimeFormatter} to use to create a {@link TemporalAccessor}
         * to which the {@link #creator} will be applied.
         */
        private final DateTimeFormatter formatter;

        /**
         * A {@link Function} that creates an object of type {@code E} from a {@link TemporalAccessor}.
         */
        private final Function<TemporalAccessor, E> creator;

        /**
         * Constructor.
         *
         * @param formatter The {@link DateTimeFormatter} to use to create a {@link TemporalAccessor}
         *                  to which the {@link #creator} will be applied.
         * @param creator   A {@link Function} that creates an object of type {@code E} from a {@link TemporalAccessor}.
         */
        private Java8TimeParamConverter(DateTimeFormatter formatter, Function<TemporalAccessor, E> creator) {
            this.formatter = formatter;
            this.creator = creator;
        }

        @Override
        public E fromString(String value) {
            return Optional.ofNullable(value)
                    .map(formatter::parse)
                    .map(creator)
                    .orElse(null);
        }

        @Override
        public String toString(E value) {
            return formatter.format(value);
        }
    }
}
