package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.net.URI;

/**
 * {@link com.fasterxml.jackson.databind.JsonSerializer} to transform a {@link URI} to a {@link String}.
 */
public class URISerializer extends StdSerializer<URI> {

    /**
     * Default constructor.
     */
    public URISerializer() {
        super(URI.class);
    }

    @Override
    public void serialize(URI value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(value.toString());
    }
}