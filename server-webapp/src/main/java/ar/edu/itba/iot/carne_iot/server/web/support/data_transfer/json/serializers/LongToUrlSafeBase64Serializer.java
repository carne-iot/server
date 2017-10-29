package ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers;

import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.Base64UrlHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * {@link com.fasterxml.jackson.databind.JsonSerializer} to transform a {@link Long} to a {@link String},
 * applying base64url encoding.
 */
public class LongToUrlSafeBase64Serializer extends StdSerializer<Long> {

    /**
     * Default constructor.
     */
    public LongToUrlSafeBase64Serializer() {
        super(Long.class);
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(Base64UrlHelper.encodeFromNumber(value, Object::toString));
    }
}