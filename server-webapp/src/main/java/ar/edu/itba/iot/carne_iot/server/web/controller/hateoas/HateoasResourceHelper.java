package ar.edu.itba.iot.carne_iot.server.web.controller.hateoas;

import org.springframework.hateoas.Resource;

import java.util.function.Function;

/**
 * Helper class in charge of providing methods to generate HATEOAS resources.
 */
public class HateoasResourceHelper {

    /**
     * Generates a {@link Resource} from a given {@link Resoursable},
     * adding a self referencing {@link org.springframework.hateoas.Link}.
     *
     * @param entity          The {@link Resoursable} from which the {@link Resource} will be built.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param <T>             The concrete type of the entity.
     * @return The created {@link Resource}.
     */
    public static <T extends Resoursable> Resource<T> toIdentifiableResource(T entity, Function<T, ?> getIdFunction,
                                                                             Class<?> controllerClass) {
        return toIdentifiableResource(entity, getIdFunction, controllerClass, new Object[0]);
    }

    /**
     * Generates a {@link Resource} from a given {@link Resoursable},
     * adding a self referencing {@link org.springframework.hateoas.Link}.
     *
     * @param entity          The {@link Resoursable} from which the {@link Resource} will be built.
     * @param getIdFunction   The {@link Function} that generates an identification from the {@code entity}.
     * @param controllerClass The {@link Class} of the controller in charge of the given {@code entity}.
     * @param parameters      An array of {@link Object} for expanding the uri template declared in the controller.
     * @param <T>             The concrete type of the entity.
     * @return The created {@link Resource}.
     */
    public static <T extends Resoursable> Resource<T> toIdentifiableResource(T entity, Function<T, ?> getIdFunction,
                                                                             Class<?> controllerClass,
                                                                             Object... parameters) {
        final Resource<T> resource = new Resource<>(entity);
        resource.add(LinkCreator.createSelfLink(entity, getIdFunction, controllerClass, parameters));

        return resource;
    }

}
