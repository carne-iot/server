package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers.URISerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.net.URI;

/**
 * Data transfer object for {@link Device} class, including registration data.
 */
public class RegisteredDeviceDto extends DeviceDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ownerUsername;

    @SuppressWarnings("unused")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = URISerializer.class)
    private URI ownerUrl;


    public RegisteredDeviceDto() {
        // For Jersey
    }

    /**
     * Constructor for registered devices.
     *
     * @param wrapper     The  {@link DeviceService.RegisteredDeviceWrapper} from which the dto will be built.
     * @param locationUrl The location url (in {@link URI} format) of the given {@link Device}.
     * @param ownerUrl    The location url (in {@link URI} format) of the {@link User} owning the device.
     */
    public RegisteredDeviceDto(DeviceService.RegisteredDeviceWrapper wrapper, URI locationUrl, URI ownerUrl) {
        super(wrapper.getDevice(), locationUrl);
        this.ownerUsername = wrapper.getUser()
                .orElseThrow(() ->
                        new IllegalArgumentException("This constructor should be used with a registered device"))
                .getUsername();
        this.ownerUrl = ownerUrl;
    }

    /**
     * Constructor for unregistered devices.
     *
     * @param wrapper     The  {@link DeviceService.RegisteredDeviceWrapper} from which the dto will be built.
     * @param locationUrl The location url (in {@link URI} format) of the given {@link Device}.
     */
    public RegisteredDeviceDto(DeviceService.RegisteredDeviceWrapper wrapper, URI locationUrl) {
        super(wrapper.getDevice(), locationUrl);
        wrapper.getUser().ifPresent(user -> {
            throw new IllegalArgumentException("This constructor should be used with an unregistered device");
        });
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public URI getOwnerUrl() {
        return ownerUrl;
    }
}
