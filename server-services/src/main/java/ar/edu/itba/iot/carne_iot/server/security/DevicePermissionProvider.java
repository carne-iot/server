package ar.edu.itba.iot.carne_iot.server.security;

/**
 * Defines behaviour for an object that provides authorization for operating over
 * {@link ar.edu.itba.iot.carne_iot.server.models.Device} instances.
 */
public interface DevicePermissionProvider {

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * owns the {@link ar.edu.itba.iot.carne_iot.server.models.Device} with the given {@code id},
     * or if it is an admin.
     *
     * @param deviceId The id of the {@link ar.edu.itba.iot.carne_iot.server.models.Device} to be written.
     * @return {@code true} if it is the owner of the device, or if it is an admin, or {@code false} otherwise.
     */
    boolean isOwnerOrAdmin(long deviceId);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.Device}
     * is the one to which a device operation will be performed to.
     *
     * @return {@code true} if it is the own device, or {@code false} otherwise.
     */
    boolean isOwnDevice(long deviceId);
}
