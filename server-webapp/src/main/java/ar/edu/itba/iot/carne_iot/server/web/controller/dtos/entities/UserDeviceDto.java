package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService.DeviceWithNicknameWrapper;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.HateoasResourceHelper;
import ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints.UserDevicesEndpoint;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.Base64UrlHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Resource;


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
     * Private constructor from a {@link DeviceWithNicknameWrapper}, to be used to create an instance of this
     * by the {@link #asResource(DeviceWithNicknameWrapper, long)},
     * which will be sent to the client
     *
     * @param wrapper The {@link DeviceWithNicknameWrapper} with the needed data.
     */
    private UserDeviceDto(DeviceWithNicknameWrapper wrapper) {
        super(wrapper.getDevice());
        this.nickname = wrapper.getNickname();
    }

    public String getNickname() {
        return nickname;
    }


    /**
     * Factory method that builds a {@link Resource} of {@link UserDeviceDto}
     * from a given {@link DeviceWithNicknameWrapper}.
     *
     * @param wrapper The {@link DeviceWithNicknameWrapper} from which the resource will be built.
     * @return A {@link Resource} of {@link UserDeviceDto}.
     */
    public static Resource<UserDeviceDto> asResource(DeviceWithNicknameWrapper wrapper, long ownerId) {

        return HateoasResourceHelper.toIdentifiableResource(new UserDeviceDto(wrapper),
                dto -> Base64UrlHelper.encodeFromNumber(dto.getId(), Object::toString),
                UserDevicesEndpoint.class,
                ownerId);
    }
}
