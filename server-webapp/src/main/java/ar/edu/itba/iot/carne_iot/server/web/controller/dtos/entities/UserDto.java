package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.Role;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.HateoasResourceHelper;
import ar.edu.itba.iot.carne_iot.server.web.controller.hateoas.Resoursable;
import ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints.UserEndpoint;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.deserializers.Java8ISOLocalDateDeserializer;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers.Java8ISOLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.Hibernate;
import org.springframework.hateoas.Resource;

import java.time.LocalDate;
import java.util.Set;

/**
 * Data transfer object for {@link User} class.
 */
public class UserDto implements Resoursable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty
    private String fullName;

    @JsonProperty
    @JsonSerialize(using = Java8ISOLocalDateSerializer.class)
    @JsonDeserialize(using = Java8ISOLocalDateDeserializer.class)
    private LocalDate birthDate;

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @SuppressWarnings("unused")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @SuppressWarnings("unused")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Role> roles;


    public UserDto() {
        // For Jersey
    }

    /**
     * Private constructor from a {@link User}, to be used to create an instance of this
     * by the {@link #asResource(User)},
     * which will be sent to the client
     *
     * @param user The {@link User} from which the dto will be built.
     */
    private UserDto(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.birthDate = user.getBirthDate();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = Hibernate.isInitialized(user.getRoles()) ? user.getRoles() : null;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    /**
     * @return The identification for the {@link User} being represented by this dto.
     */
    private long getIdentification() {
        if (this.id == null) {
            throw new IllegalStateException("This method must be called when the id is loaded");
        }

        return this.id;
    }

    /**
     * Factory method that builds a {@link Resource} of {@link UserDto} from a given {@link User}.
     *
     * @param user The {@link User} from which the resource will be built.
     * @return A {@link Resource} of {@link UserDto}.
     */
    public static Resource<UserDto> asResource(User user) {
        return HateoasResourceHelper
                .toIdentifiableResource(new UserDto(user), UserDto::getIdentification, UserEndpoint.class);
    }

    /**
     * Data transfer object for getting password change values (i.e current password and new password)
     */
    public static final class PasswordChangeDto {

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String currentPassword;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String newPassword;

        public PasswordChangeDto() {
            // For Jersey
        }

        public String getCurrentPassword() {
            return currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }
    }
}
