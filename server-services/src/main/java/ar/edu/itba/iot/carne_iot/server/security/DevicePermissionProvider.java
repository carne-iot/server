package ar.edu.itba.iot.carne_iot.server.security;

/**
 * Defines behaviour for an object that provides authorization for operating over
 * {@link ar.edu.itba.iot.carne_iot.server.models.User} instances.
 */
public interface DevicePermissionProvider {

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * owns the {@link ar.edu.itba.iot.carne_iot.server.models.Device} with the given {@code id},
     * or if it is an admin.
     *
     * @param deviceId he id of the {@link ar.edu.itba.iot.carne_iot.server.models.Device} to be written.
     * @return {@code true} if it is the owner of the device, or if it is an admin, or {@code false} otherwise.
     */
    boolean isOwnerOrAdmin(long deviceId);
}
