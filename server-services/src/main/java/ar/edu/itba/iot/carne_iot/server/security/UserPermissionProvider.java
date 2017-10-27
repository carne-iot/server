package ar.edu.itba.iot.carne_iot.server.security;

/**
 * Defines behaviour for an object that provides authorization for operating over
 * {@link ar.edu.itba.iot.carne_iot.server.models.User} instances.
 */
public interface UserPermissionProvider {

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * can read the {@link ar.edu.itba.iot.carne_iot.server.models.User} with the given {@code id}.
     *
     * @param id The id of the {@link ar.edu.itba.iot.carne_iot.server.models.User} to be read.
     * @return {@code true} if it has permission, or {@code false} otherwise.
     */
    boolean readById(long id);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * can read the {@link ar.edu.itba.iot.carne_iot.server.models.User} with the given {@code username}.
     *
     * @param username The username of the {@link ar.edu.itba.iot.carne_iot.server.models.User} to be read.
     * @return {@code true} if it has permission, or {@code false} otherwise.
     */
    boolean readByUsername(String username);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * can read the {@link ar.edu.itba.iot.carne_iot.server.models.User} with the given {@code email}.
     *
     * @param email The email of the {@link ar.edu.itba.iot.carne_iot.server.models.User} to be read.
     * @return {@code true} if it has permission, or {@code false} otherwise.
     */
    boolean readByEmail(String email);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * can write the {@link ar.edu.itba.iot.carne_iot.server.models.User} with the given {@code id}.
     *
     * @param id The id of the {@link ar.edu.itba.iot.carne_iot.server.models.User} to be writed.
     * @return {@code true} if it has permission, or {@code false} otherwise.
     */
    boolean writeById(long id);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * can delete the {@link ar.edu.itba.iot.carne_iot.server.models.User} with the given {@code id}.
     *
     * @param id The id of the {@link ar.edu.itba.iot.carne_iot.server.models.User} to be deleted.
     * @return {@code true} if it has permission, or {@code false} otherwise.
     */
    boolean deleteById(long id);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * can delete the {@link ar.edu.itba.iot.carne_iot.server.models.User} with the given {@code username}.
     *
     * @param username The username of the {@link ar.edu.itba.iot.carne_iot.server.models.User}
     *                 to be deleted.
     * @return {@code true} if it has permission, or {@code false} otherwise.
     */
    boolean deleteByUsername(String username);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * can delete the {@link ar.edu.itba.iot.carne_iot.server.models.User} with the given {@code email}.
     *
     * @param email The email of the {@link ar.edu.itba.iot.carne_iot.server.models.User} to be deleted.
     * @return {@code true} if it has permission, or {@code false} otherwise.
     */
    boolean deleteByEmail(String email);

    /**
     * Tells whether the currently authenticated {@link ar.edu.itba.iot.carne_iot.server.models.User}
     * is admin (i.e (i.e has {@link ar.edu.itba.iot.carne_iot.server.models.Role#ROLE_ADMIN} role)
     *
     * @return {@code true} if it is admin, or {@code false} otherwise.
     */
    boolean isAdmin();
}
