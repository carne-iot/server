package ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints;

import ar.edu.itba.iot.carne_iot.server.error_handling.helpers.ValidationExceptionThrower;
import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService;
import ar.edu.itba.iot.carne_iot.server.services.UserService;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.DeviceDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.RegisteredDeviceDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.StringValueDto;
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
import org.springframework.util.Base64Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
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

    @Context
    private UriInfo uriInfo;

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
                .map(toWrapper())
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

        return optional.map(toWrapper()).map(Response::ok)
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
                .map(device -> Response
                        .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(device.getId())).build()))
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
    @Path("/{id : .+}/cooking")
    public Response startCooking(@PathParam("id") @Base64url final Long id) {
        if (id == null) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Device with id {} started cooking", id);

        deviceService.startCooking(id);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id : .+}/cooking")
    public Response stopCooking(@PathParam("id") @Base64url final Long id) {
        if (id == null) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Device with id {} stopped cooking", id);

        deviceService.stopCooking(id);

        return Response.noContent().build();
    }

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


    // ======================================
    // Helper Methods
    // ======================================


    /**
     * Returns a {@link Function} that takes a {@link DeviceService.RegisteredDeviceWrapper}
     * and creates a {@link RegisteredDeviceDto} according to the {@link Device} it takes.
     *
     * @return The said function.
     */
    private Function<DeviceService.RegisteredDeviceWrapper, RegisteredDeviceDto> toWrapper() {
        return (wrapper -> {
            if (wrapper.getUser().isPresent()) {
                return new RegisteredDeviceDto(wrapper, getDeviceLocationUri(wrapper.getDevice(), uriInfo),
                        getUserLocationUri(wrapper.getUser().get(), uriInfo));
            }
            return new RegisteredDeviceDto(wrapper, getDeviceLocationUri(wrapper.getDevice(), uriInfo));
        });
    }

    /**
     * Gets the base64url representation of the given {@link Device} id.
     *
     * @param device The {@link Device} whose id in base64url must be calculated.
     * @return The base64url representation of the given {@code device}.
     */
    /* package */
    static String toBase64UrlId(Device device) {
        return Base64Utils.encodeToUrlSafeString(Long.toString(device.getId()).getBytes());
    }

    /**
     * Returns the location {@link URI} of the given {@link Device}
     * according to the context hold by the given {@link UriInfo}
     *
     * @param device  The {@link Device}'s whose location {@link URI} must be retrieved.
     * @param uriInfo The {@link UriInfo} holding the context.
     * @return The location {@link URI} of the given {@link Device}
     */
    private static URI getDeviceLocationUri(Device device, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().clone()
                .path(DEVICES_ENDPOINT)
                .path(toBase64UrlId(device))
                .build();
    }

    /**
     * Returns the location {@link URI} of the given {@link User}
     * according to the context hold by the given {@link UriInfo}
     *
     * @param user    The {@link User}'s whose location {@link URI} must be retrieved.
     * @param uriInfo The {@link UriInfo} holding the context.
     * @return The location {@link URI} of the given {@link User}
     */
    private static URI getUserLocationUri(User user, UriInfo uriInfo) {
        return UserEndpoint.baseUserUriBuilder(user.getId(), uriInfo).build();
    }
}
