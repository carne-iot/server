package ar.edu.itba.iot.carne_iot.server.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * A Custom {@link javax.servlet.Filter} to create a request given a method in a header from a POST request.
 * Idea taken from {@link org.springframework.web.filter.HiddenHttpMethodFilter},
 * using headers instead of form fields.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class CustomHiddenMethodFilter extends OncePerRequestFilter {

    /**
     * Default method parameter: {@code _method}
     */
    private static final String DEFAULT_METHOD_PARAM = "X-Hidden-Method";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHiddenMethodFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        HttpServletRequest requestToUse = request;

        if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
            String paramValue = request.getHeader(DEFAULT_METHOD_PARAM);
            if (StringUtils.hasLength(paramValue)) {
                LOGGER.debug("Changing hidden method request to a {} request", paramValue);
                requestToUse = new CustomHiddenMethodFilter.HttpMethodRequestWrapper(request, paramValue);
            }
        }
        filterChain.doFilter(requestToUse, response);
    }

    /**
     * Simple {@link HttpServletRequest} wrapper that returns the supplied method for
     * {@link HttpServletRequest#getMethod()}.
     */
    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

        private final String method;

        private HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method.toUpperCase(Locale.ENGLISH);
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
