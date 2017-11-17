package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilder;

/**
 * Created by Juan Marcos Bellini on 17/11/17.
 * Questions at jbellini@bellotapps.com
 */
public class HateoasHelper {

    /* package */
    static <T extends IdentifiableResoursable> Resource<T> toIdentifiableResource(T entity, Class<?> controllerClass) {
        return toIdentifiableResource(entity, controllerClass, new Object[0]);
    }

    /* package */
    static <T extends IdentifiableResoursable> Resource<T> toIdentifiableResource(T entity, Class<?> controllerClass,
                                                                                  Object... parameters) {
        final Resource<T> resource = new Resource<>(entity);
        final Link selfLink = JaxRsLinkBuilder.linkTo(controllerClass, parameters)
                .slash(entity.getIdentification())
                .withSelfRel();

        resource.add(selfLink);

        return resource;
    }

    /**
     * Interface used to mark a class that can be transformed into a {@link Resource}.
     */
    /* package */  interface Resoursable {
    }

    /**
     * Interface used to mark a class, that can be identified with some parameter,
     * that can be transformed into a {@link Resource}.
     */
    /* package */ interface IdentifiableResoursable<T> extends Resoursable {
        /**
         * @return The identification of the entity implementing this interface.
         */
        T getIdentification();
    }
}
