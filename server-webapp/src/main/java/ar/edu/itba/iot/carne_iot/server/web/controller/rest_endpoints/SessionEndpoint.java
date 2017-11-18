package ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints;

import ar.edu.itba.iot.carne_iot.server.services.LoginService;
import ar.edu.itba.iot.carne_iot.server.services.SessionService;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.authentication.LoginCredentialsDto;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.Base64url;
import ar.edu.itba.iot.carne_iot.server.web.support.annotations.JerseyController;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.Base64UrlHelper;
import ar.edu.itba.iot.carne_iot.server.web.support.exceptions.IllegalParamValueException;
import ar.edu.itba.iot.carne_iot.server.web.support.exceptions.MissingJsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * API endpoint for sessions management.
 */
@Path(SessionEndpoint.SESSIONS_ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
@JerseyController
public class SessionEndpoint {

    /**
     * Endpoint for session management.
     */
    public static final String SESSIONS_ENDPOINT = "/auth";

    /**
     * Endpoint for login mechanism.
     */
    public static final String LOGIN_ENDPOINT = "/login";

    /**
     * Endpoint for logout mechanism.
     */
    public static final String LOGOUT_ROOT_ENDPOINT = "/logout";


    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    /**
     * Service in charge of performing password authentication.
     */
    private final LoginService loginService;

    /**
     * Service in charge of managing sessions.
     */
    private final SessionService sessionService;


    @Autowired
    public SessionEndpoint(LoginService loginService, SessionService sessionService) {
        this.loginService = loginService;
        this.sessionService = sessionService;
    }


    @POST
    @Path(LOGIN_ENDPOINT)
    public Response login(LoginCredentialsDto loginCredentialsDto) {
        if (loginCredentialsDto == null) {
            throw new MissingJsonException();
        }

        LOGGER.debug("Trying to log in user with username {}", loginCredentialsDto.getUsername());

        // Create a JWT (i.e this creates a "session")
        final LoginService.UserTokenAndJtiContainer container = loginService
                .login(loginCredentialsDto.getUsername(), loginCredentialsDto.getPassword());

        LOGGER.debug("User {} successfully logged in", container.getUser().getUsername());

        // Generate url to perform logout of the new generated session
        final URI logoutURI = JaxRsLinkBuilder.linkTo(this.getClass())
                .slash(LOGOUT_ROOT_ENDPOINT)
                .slash(container.getUser().getId())
                .slash(Base64UrlHelper.encodeFromNumber(container.getJti(), Object::toString)).toUri();

        return Response.noContent()
                .header("X-Token", container.getToken())
                .header("X-Logout-Url", logoutURI.toString())
                .build();
    }

    @DELETE
    @Path(LOGOUT_ROOT_ENDPOINT + "/{userId : \\d+}/{jti: .+}")
    public Response logout(@SuppressWarnings("RSReferenceInspection") @PathParam("userId") final long userId,
                           @SuppressWarnings("RSReferenceInspection") @PathParam("jti") @Base64url final Long jti) {
        final List<String> wrongParams = new LinkedList<>();
        if (userId <= 0) {
            wrongParams.add("id");
        }
        if (jti == null) {
            wrongParams.add("jti");
        }
        if (!wrongParams.isEmpty()) {
            throw new IllegalParamValueException(wrongParams);
        }

        LOGGER.debug("Trying to log out user with id {}", userId);

        //noinspection ConstantConditions
        sessionService.invalidateSession(userId, jti); // if jti was null, exception would have being thrown.

        return Response.noContent().build();
    }
}
