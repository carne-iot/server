package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;

/**
 * {@link com.fasterxml.jackson.databind.JsonSerializer} to transform an {@link Instant} to a {@link String},
 * using the {@link Instant#getEpochSecond()} transformed into {@link String}.
 */
public class Java8InstantSerializer extends StdSerializer<Instant> {

    /**
     * Default constructor.
     */
    public Java8InstantSerializer() {
        super(Instant.class);
    }

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(Long.toString(value.getEpochSecond()));
    }
}