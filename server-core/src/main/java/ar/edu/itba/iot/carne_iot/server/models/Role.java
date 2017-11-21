package ar.edu.itba.iot.carne_iot.server.models;

/**
 * Enum containing the different roles a {@link User} can have (i.e the different authorities it has).
 */
public enum Role {
    /**
     * Indicates a {@link User} is a normal user.
     */
    ROLE_USER,
    /**
     * Indicates a {@link User} is an administrator.
     */
    ROLE_ADMIN,
    /**
     * Indicates that the owner of this role has permission to operate as a {@link Device}.
     */
    ROLE_DEVICE,
}
