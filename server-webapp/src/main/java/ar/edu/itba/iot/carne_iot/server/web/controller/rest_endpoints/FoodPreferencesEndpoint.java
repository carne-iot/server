package ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints;


import ar.edu.itba.iot.carne_iot.server.models.FoodPreference;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.FoodPreferencesService;
import ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities.FoodPreferenceDto;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.LinkCreator;
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
 * API endpoint for {@link FoodPreference}s management.
 */
@Path(UserEndpoint.USERS_ENDPOINT + "/{id : \\d+}" + FoodPreferencesEndpoint.FOOD_PREFERENCES_ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
@JerseyController
public class FoodPreferencesEndpoint {

    public static final String FOOD_PREFERENCES_ENDPOINT = "/food-preferences";

    /**
     * The {@link Logger} object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FoodPreferencesEndpoint.class);

    /**
     * The {@link FoodPreferencesService}.
     */
    private final FoodPreferencesService foodPreferencesService;

    @Autowired
    public FoodPreferencesEndpoint(FoodPreferencesService foodPreferencesService) {
        this.foodPreferencesService = foodPreferencesService;
    }


    // ================================================================
    // API Methods
    // ================================================================

    // ======================================
    // Basic User Operation
    // ======================================


    @GET
    public Response findByOwner(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                                @PaginationParam final Pageable pageable) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        LOGGER.debug("Listing food preferences belonging to user with id {}", id);

        final Page<FoodPreference> preferences = foodPreferencesService.findByOwner(id, pageable);

        return Response.ok(preferences.getContent().stream()
                .map(wrapper -> FoodPreferenceDto.asResource(wrapper, id))
                .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/{name : .+}")
    public Response findByName(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                               @SuppressWarnings("RSReferenceInspection") @PathParam("name") final String name) {
        validateFoodPreferenceParams(id, name);

        LOGGER.debug("Getting food preference with name {} belonging to user with id {}", name, id);

        return foodPreferencesService.findByNameAndOwner(name, id)
                .map(wrapper -> FoodPreferenceDto.asResource(wrapper, id))
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND).entity(""))
                .build();
    }

    @POST
    public Response create(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                           final FoodPreferenceDto foodPreferenceDto) {
        if (id <= 0) {
            throw new IllegalParamValueException(Collections.singletonList("id"));
        }

        return Optional.ofNullable(foodPreferenceDto)
                .map(dto -> {
                    LOGGER.debug("Creating food preference for user with id {}, and name {}",
                            id, dto.getName());
                    return foodPreferencesService.create(dto.getName(), dto.getTemperature(), id);
                })
                .map(preference -> Response
                        .created(LinkCreator.createSelfURI(preference, FoodPreference::getName,
                                FoodPreferencesEndpoint.class, id))
                        .build())
                .orElseThrow(MissingJsonException::new);
    }

    @PUT
    @Path("/{name : .+}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                           @SuppressWarnings("RSReferenceInspection") @PathParam("name") final String name,
                           final FoodPreferenceDto foodPreferenceDto) {
        validateFoodPreferenceParams(id, name);

        return Optional.ofNullable(foodPreferenceDto)
                .map(dto -> {
                    LOGGER.debug("Updating food preference with name {}, belonging to user with id {}",
                            dto.getName(), id);
                    foodPreferencesService.update(id, name,
                            foodPreferenceDto.getName(), foodPreferenceDto.getTemperature());
                    return Response.noContent()
                            // Location header is added because name might be changed.
                            .location(LinkCreator
                                    .createSelfURI(dto, FoodPreferenceDto::getName, FoodPreferencesEndpoint.class, id))
                            .build();
                })
                .orElseThrow(MissingJsonException::new);
    }

    @DELETE
    @Path("/{name : .+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@SuppressWarnings("RSReferenceInspection") @PathParam("id") final long id,
                           @SuppressWarnings("RSReferenceInspection") @PathParam("name") final String name) {
        validateFoodPreferenceParams(id, name);

        LOGGER.debug("Removing food preferences with name{}, belonging to user with id {}", name, id);

        foodPreferencesService.delete(id, name);

        return Response.noContent().build();
    }


    // ======================================
    // Helper Methods
    // ======================================

    /**
     * Performs validation over the given params.
     *
     * @param userId The {@link User} id param to be validated.
     * @param name   The {@link FoodPreference} name param to be validated.
     */
    private void validateFoodPreferenceParams(long userId, String name) {
        final List<String> wrongParams = new LinkedList<>();
        if (userId <= 0) {
            wrongParams.add("id");
        }
        if (name == null) {
            wrongParams.add("name");
        }
        if (!wrongParams.isEmpty()) {
            throw new IllegalParamValueException(wrongParams);
        }
    }
}
