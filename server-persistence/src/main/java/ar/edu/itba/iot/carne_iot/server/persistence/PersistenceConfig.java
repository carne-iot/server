package ar.edu.itba.iot.carne_iot.server.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration class to set up persistence layer.
 */
@Configuration
@EnableJpaRepositories(basePackages = {
        "ar.edu.itba.iot.carne_iot.server.persistence.daos",
})
@EntityScan("ar.edu.itba.iot.carne_iot.server.models")
public class PersistenceConfig {
}
