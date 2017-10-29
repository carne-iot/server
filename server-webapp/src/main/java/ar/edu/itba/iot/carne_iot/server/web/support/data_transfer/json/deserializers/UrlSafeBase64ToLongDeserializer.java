package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.deserializers;

import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.Base64UrlHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

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
        return Base64UrlHelper.decodeToNumber(p.getText(), Long::valueOf);
    }
}