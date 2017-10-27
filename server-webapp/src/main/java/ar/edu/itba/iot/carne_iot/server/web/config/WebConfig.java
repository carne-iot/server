package ar.edu.itba.iot.carne_iot.server.web.config;

import com.bellotapps.utils.error_handler.EnableErrorHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class in charge of configuring web concerns.
 */
@Configuration
@ComponentScan({
        "ar.edu.itba.iot.carne_iot.server.web.controller",
})
@EnableErrorHandler(basePackages = "ar.edu.itba.iot.carne_iot.server.web.error_handlers")
public class WebConfig {
}
