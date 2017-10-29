package ar.edu.itba.iot.carne_iot.server.security;

/**
 * Defines behaviour for an object that provides authorization for operating over
 * {@link ar.edu.itba.iot.carne_iot.server.models.User} instances.
 */
public interface AdminPermissionProvider {

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * is admin (i.e (i.e has {@link ar.edu.itba.iot.carne_iot.server.models.Role#ROLE_ADMIN} role)
     *
     * @return {@code true} if it is admin, or {@code false} otherwise.
     */
    boolean isAdmin();
}
