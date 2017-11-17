package ar.edu.itba.iot.carne_iot.server.application;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Bootstrap class.
 */
@SpringBootApplication(
        exclude = {
                WebMvcAutoConfiguration.class,
                DispatcherServletAutoConfiguration.class,
                ErrorMvcAutoConfiguration.class,
        }
)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .bannerMode(Banner.Mode.OFF)
                .build().run(args);
    }
}
