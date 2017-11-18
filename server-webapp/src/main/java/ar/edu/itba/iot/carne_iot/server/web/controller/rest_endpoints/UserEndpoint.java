package ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.Role;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService;
import ar.edu.itba.iot.carne_iot.server.services.UserService;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.StringValueDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.UserDeviceDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.UserDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.LinkCreator;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.Base64url;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.Java8Time;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.JerseyController;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.PaginationParam;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.DateTimeFormatters;
import ar.edu.itba.iot.carne_iot.server.web.support.exceptions.IllegalParamValueException;
import ar.edu.itba.iot.carne_iot.server.web.support.exceptions.MissingJsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * API endpoint for {@link User}s management.
 */
@Path(UserEndpoint.USERS_ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
@JerseyController
public class UserEndpoint {

    /**
     * Endpoint for {@link User} management.
     */
    public static final String USERS_ENDPOINT = "/users";

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    @Context
    private UriInfo uriInfo;

    /**
     * The {@link UserService}.
     */
    private final UserService userService;

    /**
     * The {@link DeviceService}.
     */
    private final DeviceService deviceService;

    @Autowired
    public UserEndpoint(UserService userService, DeviceService deviceService) {
        this.userService = userService;
        this.deviceService = deviceService;
    }


    // ================================================================
    // API Methods
    // ================================================================

    // ======================================
    // Basic User Operation
    // ======================================

    @GET
    @Path("/fuck")
    public Response testing() {
        return userService.getById(2)
                .map(UserDto::asResource)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }


    @GET
    public Response findMatching(@QueryParam("fullName") final String fullName,
                                 @SuppressWarnings("RestParamTypeInspection")
                                 @QueryParam("minBirthDate")
                                 @Java8Time(formatter = DateTimeFormatters.ISO_LOCAL_DATE) final LocalDate minBirthDate,
                                 @SuppressWarnings("RestParamTypeInspection")
                                 @QueryParam("maxBirthDate")
                                 @Java8Time(formatter = DateTimeFormatters.ISO_LOCAL_DATE) final LocalDate maxBirthDate,
                                 @QueryParam("username") final String username,
                                 @QueryParam("email") final String email,
                                 @PaginationParam final Pageable pageable) {
        LOGGER.debug("Getting users matching");

        final Page<User> users = userService
                .findMatching(fullName, minBirthDate, maxBirthDate, username, email, pageable);

        return Response.ok(users.getContent().stream()
                .map(UserDto::asResource)
                .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("{id : \\d+}")
    public Response getUserById(@PathParam("id") final long id) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Getting user by id {}", id);

        return toUserResponse(userService.getById(id));
    }

    @GET
    @Path("username/{username : .+}")
    public Response getUserByUsername(@PathParam("username") final String username) {
        if (username == null) {
            throw new IllegalParamValueException(Collections.singletonList("username"));
        }

        LOGGER.debug("Getting user by username {}", username);

        return toUserResponse(userService.getByUsername(username));
    }

    @GET
    @Path("email/{email : .+}")
    public Response getUserByEmail(@PathParam("email") final String email) {
        if (email == null) {
            throw new IllegalParamValueException(Collections.singletonList("email"));
        }

        LOGGER.debug("Getting user by email {}", email);

        return toUserResponse(userService.getByEmail(email));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(final UserDto userDto) {
        return Optional.ofNullable(userDto)
                .map(dto -> {
                    LOGGER.debug("Creating user with username {}, and email {}",
                            userDto.getUsername(), userDto.getEmail());
                    return userService.register(userDto.getFullName(), userDto.getBirthDate(),
                            userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
                })
                .map(user -> Response
                        .created(LinkCreator.createSelfURI(user, User::getId, UserEndpoint.class))
                        .build())
                .orElseThrow(MissingJsonException::new);
    }

    @PUT
    @Path("{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") final long id, final UserDto userDto) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        return Optional.ofNullable(userDto)
                .map(dto -> {
                    LOGGER.debug("Updating user with id {}", id);
                    userService.update(id, userDto.getFullName(), userDto.getBirthDate());
                    return Response.noContent().build();
                })
                .orElseThrow(MissingJsonException::new);
    }


    @PUT
    @Path("{id : \\d+}/username")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeUsername(@PathParam("id") final long id, final StringValueDto newUsernameDto) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        return Optional.ofNullable(newUsernameDto)
                .map(StringValueDto::getValue)
                .map(username -> {
                    LOGGER.debug("Changing username to {} to user with id {}", username, id);
                    userService.changeUsername(id, username);
                    return Response.noContent().build();
                })
                .orElseThrow(MissingJsonException::new);
    }

    @PUT
    @Path("{id : \\d+}/email")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeEmail(@PathParam("id") final long id, final StringValueDto newEmailDto) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        return Optional.ofNullable(newEmailDto)
                .map(StringValueDto::getValue)
                .map(email -> {
                    LOGGER.debug("Changing username to {} to user with id {}", email, id);
                    userService.changeEmail(id, email);
                    return Response.noContent().build();
                })
                .orElseThrow(MissingJsonException::new);
    }

    @PUT
    @Path("{id : \\d+}/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(@PathParam("id") final long id, final UserDto.PasswordChangeDto passwordChangeDto) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        return Optional.ofNullable(passwordChangeDto)
                .map(dto -> {
                    LOGGER.debug("Changing password to user with id {} ", id);
                    userService.changePassword(id, dto.getCurrentPassword(), dto.getNewPassword());
                    return Response.noContent().build();
                })
                .orElseThrow(MissingJsonException::new);
    }

    @GET
    @Path("{id : \\d+}/authorities")
    public Response getAuthorities(@PathParam("id") final long id) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Getting authorities for user with id {} ", id);

        final Set<Role> roles = userService.getRoles(id);

        return Response.ok(roles).build();
    }

    @PUT
    @Path("{id : \\d+}/authorities/{role: .+}")
    public Response addAuthority(@PathParam("id") final long id, @PathParam("role") final Role role) {
        validateRoleParams(id, role);

        LOGGER.debug("Adding role {} to user with id {} ", role, id);

        userService.addRole(id, role);

        return Response.noContent().build();
    }

    @DELETE
    @Path("{id : \\d+}/authorities/{role: .+}")
    public Response removeAuthority(@PathParam("id") final long id, @PathParam("role") final Role role) {
        validateRoleParams(id, role);

        LOGGER.debug("Removing role {} to user with id {} ", role, id);

        userService.removeRole(id, role);

        return Response.noContent().build();
    }

    @DELETE
    @Path("{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") final long id) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Removing user with id {}", id);

        userService.deleteById(id);

        return Response.noContent().build();
    }

    @DELETE
    @Path("username/{username : .+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteByUsername(@PathParam("username") final String username) {
        if (username == null) {
            throw new IllegalParamValueException(Collections.singletonList("username"));
        }

        LOGGER.debug("Removing user with username {}", username);

        userService.deleteByUsername(username);

        return Response.noContent().build();
    }

    @DELETE
    @Path("email/{email : .+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteByEmail(@PathParam("email") final String email) {
        if (email == null) {
            throw new IllegalParamValueException(Collections.singletonList("username"));
        }

        LOGGER.debug("Removing user with username {}", email);

        userService.deleteByEmail(email);

        return Response.noContent().build();
    }

    // ======================================
    // User devices
    // ======================================

    @GET
    @Path("/{id : \\d+}" + DevicesEndpoint.DEVICES_ENDPOINT)
    public Response listUserDevices(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                                    @PaginationParam final Pageable pageable) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Listing devices belonging to user with id {}", id);

        final Page<DeviceService.DeviceWithNicknameWrapper> devices = deviceService.listUserDevices(id, pageable);

        return Response.ok(devices.getContent().stream()
                .map(wrapper ->
                        new UserDeviceDto(wrapper, getUserDeviceLocationUri(id, wrapper.getDevice(), uriInfo)))
                .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/{id : \\d+}" + DevicesEndpoint.DEVICES_ENDPOINT + "/{deviceId : .+}")
    public Response getDevice(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                              @SuppressWarnings("RSReferenceInspection")
                              @PathParam("deviceId") @Base64url final Long deviceId) {
        validateUserDeviceParams(id, deviceId);

        LOGGER.debug("Getting device with id {} belonging to user with id {}", deviceId, id);

        final Optional<DeviceService.DeviceWithNicknameWrapper> wrapperOptional =
                deviceService.getRegisteredDevice(id, deviceId);

        return wrapperOptional.map(wrapper ->
                Response.ok(new UserDeviceDto(wrapper, getUserDeviceLocationUri(id, wrapper.getDevice(), uriInfo))))
                .orElse(Response.status(Response.Status.NOT_FOUND).entity(""))
                .build();
    }

    @PUT
    @Path("/{id : \\d+}" + DevicesEndpoint.DEVICES_ENDPOINT + "/{deviceId : .+}")
    public Response registerDevice(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                                   @SuppressWarnings("RSReferenceInspection")
                                   @PathParam("deviceId") @Base64url final Long deviceId) {
        validateUserDeviceParams(id, deviceId);

        LOGGER.debug("Registering device with id {} belonging to user with id {}", deviceId, id);

        deviceService.registerDevice(id, deviceId);

        return Response.noContent().build();
    }

    @PUT
    @Path("/{id : \\d+}" + DevicesEndpoint.DEVICES_ENDPOINT + "/{deviceId : .+}/nickname")
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
    @Path("/{id : \\d+}" + DevicesEndpoint.DEVICES_ENDPOINT + "/{deviceId : .+}/nickname")
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
     * Returns a {@link Response} according to the given {@code userOptional} content.
     *
     * @param userOptional The {@link Optional} that might hold a {@link User}.
     * @return A {@link Response} containing the {@link UserDto}
     * created with the held in the given {@link Optional} if present,
     * or a {@link Response.Status#NOT_FOUND} {@link Response} otherwise.
     */
    private static Response toUserResponse(@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
                                                   Optional<User> userOptional) {
        return userOptional.map(UserDto::asResource)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND).entity(""))
                .build();
    }

    /**
     * Returns a {@link UriBuilder} for a given {@link User} location.
     *
     * @param userId  The id of {@link User}'s whose location {@link UriBuilder} must be retrieved.
     * @param uriInfo The {@link UriInfo} holding the context.
     * @return The initialized {@link UriBuilder}.
     */
    /* package */
    static UriBuilder baseUserUriBuilder(long userId, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().clone()
                .path(USERS_ENDPOINT)
                .path(Long.toString(userId));
    }

    /**
     * Returns the location {@link URI} of the given {@link User}
     * according to the context hold by the given {@link UriInfo}
     *
     * @param user    The {@link User}'s whose location {@link URI} must be retrieved.
     * @param uriInfo The {@link UriInfo} holding the context.
     * @return The location {@link URI} of the given {@link User}
     */
    private static URI getLocationUri(User user, UriInfo uriInfo) {
        return baseUserUriBuilder(user.getId(), uriInfo)
                .build();
    }

    /**
     * Returns the location {@link URI} of the given {@link Device} of the given {@link User},
     * according to the context hold by the given {@link UriInfo}
     *
     * @param userId  The id of {@link User}'s whose location {@link URI} must be retrieved.
     * @param device  The {@link Device}'s whose location {@link URI} must be retrieved.
     * @param uriInfo The {@link UriInfo} holding the context.
     * @return The location {@link URI} of the given {@link User}
     */
    private static URI getUserDeviceLocationUri(long userId, Device device, UriInfo uriInfo) {
        return baseUserUriBuilder(userId, uriInfo)
                .path(DevicesEndpoint.DEVICES_ENDPOINT)
                .path(DevicesEndpoint.toBase64UrlId(device))
                .build();
    }

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

    /**
     * Performs validation over the given params.
     *
     * @param id   The {@link User} id param to be validated.
     * @param role The {@link Role} param to be validated.
     * @throws IllegalParamValueException If any of the params is not valid.
     */
    private static void validateRoleParams(long id, Role role) throws IllegalParamValueException {
        final List<String> paramErrors = new LinkedList<>();
        if (id <= 0) {
            paramErrors.add("id");
        }
        if (role == null) {
            paramErrors.add("role");
        }
        if (!paramErrors.isEmpty()) {
            throw new IllegalParamValueException(paramErrors);
        }
    }
}
