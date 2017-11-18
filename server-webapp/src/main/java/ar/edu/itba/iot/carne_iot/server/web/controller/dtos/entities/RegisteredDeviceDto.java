package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService.RegisteredDeviceWrapper;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.HateoasResourceHelper;
import ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints.DevicesEndpoint;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.Base64UrlHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Resource;

/**
 * Data transfer object for {@link Device} class, including registration data.
 */
public class RegisteredDeviceDto extends DeviceDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private String ownerUsername;


    public RegisteredDeviceDto() {
        // For Jersey
    }

    /**
     * Private constructor from a {@link RegisteredDeviceWrapper}, to be used to create an instance of this
     * by the {@link #asResource(RegisteredDeviceWrapper)},
     * which will be sent to the client
     *
     * @param wrapper The {@link RegisteredDeviceWrapper} with the needed data.
     */
    private RegisteredDeviceDto(RegisteredDeviceWrapper wrapper) {
        super(wrapper.getDevice());
        this.ownerUsername = wrapper.getUser()
                .map(User::getUsername)
                .orElse(null);
    }

    /**
     * Factory method that builds a {@link Resource} of {@link RegisteredDeviceDto}
     * from a given {@link RegisteredDeviceWrapper}.
     *
     * @param wrapper The {@link RegisteredDeviceWrapper} from which the resource will be built.
     * @return A {@link Resource} of {@link RegisteredDeviceDto}.
     */
    public static Resource<RegisteredDeviceDto> asResource(RegisteredDeviceWrapper wrapper) {

        return HateoasResourceHelper.toIdentifiableResource(new RegisteredDeviceDto(wrapper),
                dto -> Base64UrlHelper.encodeFromNumber(dto.getId(), Object::toString),
                DevicesEndpoint.class);
    }
}
