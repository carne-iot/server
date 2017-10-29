package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.DeviceRegistration;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * Data transfer object for {@link Device} class, including {@link User} specific information of the device.
 */
public class UserDeviceDto extends DeviceDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nickname;


    public UserDeviceDto() {
        // For Jersey
    }

    /**
     * Constructor for {@link DeviceRegistration} (i.e take data from a registration object).
     *
     * @param wrapper     The {@link ar.edu.itba.iot.carne_iot.server.services.DeviceService.DeviceWithNicknameWrapper}
     *                    with the needed data.
     * @param locationUrl The location url (in {@link URI} format) of the given {@link Device}.
     */
    public UserDeviceDto(DeviceService.DeviceWithNicknameWrapper wrapper, URI locationUrl) {
        super(wrapper.getDevice(), locationUrl);
        this.nickname = wrapper.getNickname();
    }

    public String getNickname() {
        return nickname;
    }
}
