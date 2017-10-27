package ar.edu.itba.iot.carne_iot.server.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.Collection;

/**
 * Jersey configuration class. An extension of {@link ResourceConfig} that is declared as a Spring component.
 */
@Component
/* package */ class JerseyConfig extends ResourceConfig {


    @Autowired
    /* package */ JerseyConfig(ObjectMapper objectMapper, ThrowableMapper throwableMapper) {

        // Register packages with resources and providers
        registerPackages(this,
                "ar.edu.itba.iot.carne_iot.server.web.controller.rest_endpoints",
                "ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.jersey_providers");
        // Register ObjectMapper which will be used to serialize/deserialize JSON
        register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        // Register the ThrowableMapper that will wire the exception into the error handler
        register(throwableMapper);
    }

    /**
     * Registers the classes annotated with the {@link Provider} annotation in the given {@code packages}.
     * This allows package scanning with Jersey (as currently not supported by library).
     *
     * @param packages The packages containing providers.
     */
    private static void registerPackages(ResourceConfig resourceConfig, String... packages) {
        // Register packages of in app Providers
        final ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));

        Arrays.stream(packages)
                .map(scanner::findCandidateComponents).flatMap(Collection::stream)
                .map(beanDefinition ->
                        ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), resourceConfig.getClassLoader()))
                .forEach(resourceConfig::register);
    }
}
