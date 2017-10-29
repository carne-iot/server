package ar.edu.itba.iot.carne_iot.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link AdminPermissionProvider}.
 */
@Component("adminPermissionProvider")
/* package */ class AdminPermissionProviderImpl implements AdminPermissionProvider {

    @Override
    public boolean isAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && PermissionProviderHelper.isAdmin(authentication);
    }
}
