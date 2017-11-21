package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.FoodPreference;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.HateoasResourceHelper;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.Resoursable;
import ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints.FoodPreferencesEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Resource;

import java.math.BigDecimal;

/**
 * Data transfer object for {@link FoodPreference} class.
 */
public class FoodPreferenceDto implements Resoursable {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private BigDecimal temperature;

    public FoodPreferenceDto() {
        // For Jersey
    }

    /* package */ FoodPreferenceDto(FoodPreference foodPreference) {
        this.id = foodPreference.getId();
        this.name = foodPreference.getName();
        this.temperature = foodPreference.getTemperature();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * Factory method that builds a {@link Resource} of {@link FoodPreferenceDto}
     * from a given {@link FoodPreference}.
     *
     * @param foodPreference The {@link FoodPreference} from which the resource will be built.
     * @param ownerId        The id of the {@link User} owning this {@link FoodPreference}.
     * @return A {@link Resource} of {@link FoodPreferenceDto}.
     */
    public static Resource<FoodPreferenceDto> asResource(FoodPreference foodPreference, long ownerId) {
        return HateoasResourceHelper.toIdentifiableResource(new FoodPreferenceDto(foodPreference),
                FoodPreferenceDto::getName, FoodPreferencesEndpoint.class, ownerId);
    }
}
