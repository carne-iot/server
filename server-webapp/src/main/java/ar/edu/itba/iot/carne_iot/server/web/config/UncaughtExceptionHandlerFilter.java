package ar.edu.itba.iot.carne_iot.server.web.config;

import com.bellotapps.utils.error_handler.ErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * Filter that is in charge of handling gracefully any uncaught exception.
 * Will return data in JSON format.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
/* package */ class UncaughtExceptionHandlerFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(UncaughtExceptionHandlerFilter.class);

    /**
     * Indicates the type of data being returned in the response by this filter.
     */
    private static final String CONTENT_TYPE = "application/json";

    /**
     * The {@link ErrorHandler} in charge of transforming an exception into data to be returned in the response.
     */
    private final ErrorHandler handler;

    /**
     * The {@link ObjectMapper} in charge of serializing data into JSON to be returned in the response body.
     */
    private final ObjectMapper objectMapper;

    @Autowired
    /* package */ UncaughtExceptionHandlerFilter(ErrorHandler handler, ObjectMapper objectMapper) {
        this.handler = handler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) {
        try {
            chain.doFilter(req, resp);
        } catch (Throwable exception) {
            LOGGER.debug("An exception was not caught and is being handled by the UncaughtExceptionFilter", exception);
            final HttpServletResponse response = (HttpServletResponse) resp;
            if (response.isCommitted()) {
                LOGGER.error("Response was committed before handling exception");
                return;
            }
            try {
                final ErrorHandler.HandlingResult container = handler.handle(exception);
                response.setStatus(container.getHttpErrorCode());
                Optional.ofNullable(container.getErrorRepresentationEntity())
                        .ifPresent(entity -> {
                            try {
                                response.setContentType(CONTENT_TYPE);
                                objectMapper.writeValue(response.getOutputStream(), entity);
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            } catch (Throwable e) {
                LOGGER.error("Exception was thrown when handling exception!", e);
            }
        }
    }
}
