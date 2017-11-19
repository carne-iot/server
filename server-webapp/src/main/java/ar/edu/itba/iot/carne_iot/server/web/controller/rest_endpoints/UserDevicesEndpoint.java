package ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.StringValueDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.UserDeviceDto;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.Base64url;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.JerseyController;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.PaginationParam;
import ar.edu.itba.iot.carne_iot.server.web.support.exceptions.IllegalParamValueException;
import ar.edu.itba.iot.carne_iot.server.web.support.exceptions.MissingJsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * API endpoint for {@link User}s {@link Device}s management.
 */
@Path(UserEndpoint.USERS_ENDPOINT + "/{id : \\d+}" + DevicesEndpoint.DEVICES_ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
@JerseyController
public class UserDevicesEndpoint {

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDevicesEndpoint.class);

    /**
     * The {@link DeviceService}.
     */
    private final DeviceService deviceService;

    @Autowired
    public UserDevicesEndpoint(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    // ================================================================
    // API Methods
    // ================================================================


    // ======================================
    // User devices
    // ======================================

    @GET
    public Response listUserDevices(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                                    @PaginationParam final Pageable pageable) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Listing devices belonging to user with id {}", id);

        final Page<DeviceService.DeviceWithNicknameWrapper> devices = deviceService.listUserDevices(id, pageable);

        return Response.ok(devices.getContent().stream()
                .map(wrapper -> UserDeviceDto.asResource(wrapper, id))
                .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/{deviceId : .+}")
    public Response getDevice(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                              @SuppressWarnings("RSReferenceInspection")
                              @PathParam("deviceId") @Base64url final Long deviceId) {
        validateUserDeviceParams(id, deviceId);

        LOGGER.debug("Getting device with id {} belonging to user with id {}", deviceId, id);

        final Optional<DeviceService.DeviceWithNicknameWrapper> wrapperOptional =
                deviceService.getRegisteredDevice(id, deviceId);

        return wrapperOptional.map(wrapper -> UserDeviceDto.asResource(wrapper, id))
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND).entity(""))
                .build();
    }

    @PUT
    @Path("/{deviceId : .+}")
    public Response registerDevice(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                                   @SuppressWarnings("RSReferenceInspection")
                                   @PathParam("deviceId") @Base64url final Long deviceId) {
        validateUserDeviceParams(id, deviceId);

        LOGGER.debug("Registering device with id {} belonging to user with id {}", deviceId, id);

        deviceService.registerDevice(id, deviceId);

        return Response.noContent().build();
    }

    @PUT
    @Path("/{deviceId : .+}/nickname")
    public Response changeNickname(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                                   @SuppressWarnings("RSReferenceInspection")
                                   @PathParam("deviceId") @Base64url final Long deviceId,
                                   final StringValueDto stringValueDto) {
        validateUserDeviceParams(id, deviceId);
        if (stringValueDto == null) {
            throw new MissingJsonException();
        }

        LOGGER.debug("Changing nickname to {} to device with id {} belonging to user with id {}",
                stringValueDto.getValue(), deviceId, id);

        deviceService.setNickname(id, deviceId, stringValueDto.getValue());

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{deviceId : .+}/nickname")
    public Response deleteNickname(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                                   @SuppressWarnings("RSReferenceInspection")
                                   @PathParam("deviceId") @Base64url final Long deviceId) {
        validateUserDeviceParams(id, deviceId);

        LOGGER.debug("Removing nickname to device with id {} belonging to user with id {}", deviceId, id);

        deviceService.deleteNickname(id, deviceId);

        return Response.noContent().build();
    }


    // ======================================
    // Helper Methods
    // ======================================

    /**
     * Performs validation over the given params.
     *
     * @param userId   The {@link User} id param to be validated.
     * @param deviceId The {@link Device} id param to be validated.
     */
    private void validateUserDeviceParams(long userId, Long deviceId) {
        final List<String> wrongParams = new LinkedList<>();
        if (userId <= 0) {
            wrongParams.add("id");
        }
        if (deviceId == null) {
            wrongParams.add("deviceId");
        }
        if (!wrongParams.isEmpty()) {
            throw new IllegalParamValueException(wrongParams);
        }
    }
}
