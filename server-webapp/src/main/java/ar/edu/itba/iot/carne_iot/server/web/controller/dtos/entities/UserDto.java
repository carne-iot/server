package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.Role;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.deserializers.Java8ISOLocalDateDeserializer;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers.Java8ISOLocalDateSerializer;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers.URISerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.Hibernate;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

/**
 * Data transfer object for {@link User} class.
 */
public class UserDto {

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

    @SuppressWarnings("unused")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = URISerializer.class)
    private URI locationUrl;

    public UserDto() {
        // For Jersey
    }

    /**
     * Constructor.
     *
     * @param user        The {@link User} from which the dto will be built.
     * @param locationUrl The location url (in {@link URI} format) of the given {@link User}.
     */
    public UserDto(User user, URI locationUrl) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.birthDate = user.getBirthDate();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = Hibernate.isInitialized(user.getRoles()) ? user.getRoles() : null;

        this.locationUrl = locationUrl;
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
