package ar.edu.itba.iot.carne_iot.server.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security main configuration.
 */
@Configuration
@ComponentScan(basePackages = {
        "ar.edu.itba.iot.carne_iot.server.security",
        "ar.edu.itba.iot.carne_iot.server.web.security",
})
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
