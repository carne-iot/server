package ar.edu.itba.iot.carne_iot.server.web.controller.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilder;

import java.net.URI;
import java.util.function.Function;

/**
 * Helper class in charge of creating {@link Link}s and {@link URI}s.
 */
public class LinkCreator {

    /**
     * Creates a {@link Link} for a self reference.
     *
     * @param entity          The entity whose {@link Link} must be retrieved.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param <T>             The concrete type of the entity.
     * @return The created link.
     */
    public static <T> Link createSelfLink(T entity, Function<T, ?> getIdFunction, Class<?> controllerClass) {
        return createSelfLink(entity, getIdFunction, controllerClass, new Object[0]);
    }

    /**
     * Creates a {@link Link} for a self reference.
     *
     * @param entity          The entity whose {@link Link} must be retrieved.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param parameters      An array of {@link Object} for expanding the uri template declared in the controller.
     * @param <T>             The concrete type of the entity.
     * @return The created link.
     */
    public static <T> Link createSelfLink(T entity, Function<T, ?> getIdFunction, Class<?> controllerClass,
                                          Object... parameters) {
        return createLink(entity, getIdFunction, Link.REL_SELF, controllerClass, parameters);
    }

    /**
     * Creates a {@link Link} for a given reference (i.e relation is an argument).
     *
     * @param entity          The entity whose {@link Link} must be retrieved.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param <T>             The concrete type of the entity.
     * @return The created link.
     */
    public static <T> Link createLink(T entity, Function<T, ?> getIdFunction, String rel, Class<?> controllerClass) {
        return createLink(entity, getIdFunction, rel, controllerClass, new Object[0]);
    }

    /**
     * Creates a {@link Link} for a given reference (i.e relation is an argument).
     *
     * @param entity          The entity whose {@link Link} must be retrieved.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param parameters      An array of {@link Object} for expanding the uri template declared in the controller.
     * @param <T>             The concrete type of the entity.
     * @return The created link.
     */
    public static <T> Link createLink(T entity, Function<T, ?> getIdFunction, String rel, Class<?> controllerClass,
                                      Object... parameters) {
        return JaxRsLinkBuilder.linkTo(controllerClass, parameters)
                .slash(getIdFunction.apply(entity))
                .withRel(rel);
    }

    /**
     * Creates a {@link URI} for a self reference.
     * Method to be used for creating the URI for the 'Location' header.
     *
     * @param entity          The entity whose {@link Link} must be retrieved.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param <T>             The concrete type of the entity.
     * @return The created link.
     */
    public static <T> URI createSelfURI(T entity, Function<T, ?> getIdFunction, Class<?> controllerClass) {
        return createSelfURI(entity, getIdFunction, controllerClass, new Object[0]);
    }

    /**
     * Creates a {@link URI} for a self reference.
     * Method to be used for creating the URI for the 'Location' header.
     *
     * @param entity          The entity whose {@link Link} must be retrieved.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param parameters      An array of {@link Object} for expanding the uri template declared in the controller.
     * @param <T>             The concrete type of the entity.
     * @return The created link.
     */
    public static <T> URI createSelfURI(T entity, Function<T, ?> getIdFunction, Class<?> controllerClass,
                                        Object... parameters) {
        final Link link = createSelfLink(entity, getIdFunction, controllerClass, parameters);

        return URI.create(link.getHref());
    }
}
