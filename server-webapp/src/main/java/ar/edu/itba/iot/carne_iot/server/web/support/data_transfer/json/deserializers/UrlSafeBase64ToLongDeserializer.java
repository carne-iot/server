package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {@link com.fasterxml.jackson.databind.JsonDeserializer} to transform a {@link String} to a {@link Long},
 * applying Base64url decoding.
 */
public class UrlSafeBase64ToLongDeserializer extends StdDeserializer<Long> {

    /**
     * Default constructor.
     */
    protected UrlSafeBase64ToLongDeserializer() {
        super(Long.class);
    }

    @Override
    public Long deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        final String encodedString = p.getText();
        final String decodedString = new String(Base64Utils.decodeFromUrlSafeString(encodedString),
                StandardCharsets.UTF_8);
        return Long.valueOf(decodedString);
    }
}