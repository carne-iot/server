package ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints;

import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService;
import ar.edu.itba.iot.carne_iot.server.services.UserService;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.DeviceDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.RegisteredDeviceDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.StringValueDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.LinkCreator;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.Base64url;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.JerseyController;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.PaginationParam;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.Base64UrlHelper;
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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * API endpoint for {@link Device}s management.
 */
@Path(DevicesEndpoint.DEVICES_ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
@JerseyController
public class DevicesEndpoint implements ValidationExceptionThrower {

    /**
     * Endpoint for {@link User} management.
     */
    public static final String DEVICES_ENDPOINT = "/devices";

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DevicesEndpoint.class);

    /**
     * The {@link UserService}.
     */
    private final DeviceService deviceService;


    @Autowired
    public DevicesEndpoint(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    // ================================================================
    // API Methods
    // ================================================================

    // ======================================
    // Basic Device Operation
    // ======================================


    @GET
    public Response listDevices(@PaginationParam final Pageable pageable) {
        LOGGER.debug("Listing devices");

        final Page<DeviceService.RegisteredDeviceWrapper> devices = deviceService
                .listDevices(pageable);

        return Response.ok(devices.getContent().stream()
                .map(RegisteredDeviceDto::asResource)
                .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("{id : .+}")
    public Response getDeviceById(@PathParam("id") @Base64url final Long id) {
        if (id == null) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Getting device by id {}", id);

        final Optional<DeviceService.RegisteredDeviceWrapper> optional =
                deviceService.getDeviceWithRegistrationData(id);

        return optional.map(RegisteredDeviceDto::asResource)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND).entity(""))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDevice(DeviceDto deviceDto) {
        return Optional.ofNullable(deviceDto)
                .map(DeviceDto::getId)
                .map(id -> {
                    LOGGER.debug("Creating device with id {}", id);
                    return deviceService.createDevice(deviceDto.getId());
                })
                .map(device ->
                        Response.created(LinkCreator.createSelfURI(device,
                                d -> Base64UrlHelper.encodeFromNumber(d.getId(), Object::toString),
                                DevicesEndpoint.class)))
                .orElseThrow(MissingJsonException::new)
                .build();
    }

    @DELETE
    @Path("/registrations/{id : .+}")
    public Response unregisterDevice(@PathParam("id") @Base64url final Long id) {
        if (id == null) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Unregistering device with id {}", id);

        deviceService.unregisterDevice(id);

        return Response.noContent().build();
    }


    // ======================================
    // Device state changers
    // ======================================

    @PUT
    @Path("/{id : .+}/temperature")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTemperature(@PathParam("id") @Base64url final Long id, StringValueDto dto) {
        if (id == null) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }
        if (dto == null) {
            throw new MissingJsonException();
        }

        LOGGER.debug("Updating temperature of device with id {}", id);

        final BigDecimal value = new BigDecimal(dto.getValue());
        deviceService.updateTemperature(id, value);

        return Response.noContent().build();
    }

    @POST
    @Path("/{deviceId : .+}/pair")
    public Response pairDevice(@PathParam("deviceId") @Base64url final Long deviceId) {

        LOGGER.debug("Trying to pair device with id {}", deviceId);

        // Create a JWT for the paired device
        final String token = deviceService.pair(deviceId);

        LOGGER.debug("Device {} successfully paired", deviceId);

        // TODO: if token can be invalidated, add an "invalidation" url

        return Response.noContent()
                .header("X-Device-Token", token)
                .build();
    }

    @PUT
    @Path("/{id : .+}/target-temperature")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setTargetTemperature(@PathParam("id") @Base64url final Long id, StringValueDto dto) {
        if (id == null) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }
        if (dto == null) {
            throw new MissingJsonException();
        }

        LOGGER.debug("Setting target temperature of device with id {}", id);

        final BigDecimal value = new BigDecimal(dto.getValue());
        deviceService.setTargetTemperature(id, value);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id : .+}/target-temperature")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response clearTargetTemperature(@PathParam("id") @Base64url final Long id) {
        if (id == null) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Clearing target temperature of device with id {}", id);

        deviceService.clearTargetTemperature(id);

        return Response.noContent().build();
    }
}
