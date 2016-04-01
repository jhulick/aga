package gov.max.microservices.gateway.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentUserLogin() {
        String userName = null;

        Optional<Object> principalOption = getCurrentUserPrincipal();
        if (principalOption.isPresent()) {
            Object principal = principalOption.get();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                userName = (String) principal;
            }
        }
        return userName;
    }

    /**
     * Return the current user, or throws an exception, if the user is not
     * authenticated yet.
     *
     * @return the current user
     */
    public static UserDetails getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                return (UserDetails) authentication.getPrincipal();
            }
        }
        throw new IllegalStateException("User not found!");
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        return !isCurrentUserInRole(AuthoritiesConstants.ANONYMOUS);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        Optional<Authentication> authentication = getCurrentUserAuthentication();

        return authentication.isPresent()
            && authentication.get().getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }

    private static Optional<Authentication> getCurrentUserAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    private static Optional<Object> getCurrentUserPrincipal() {
        Optional<Authentication> authentication = getCurrentUserAuthentication();
        if (authentication.isPresent()) {
            return Optional.ofNullable(authentication.get().getPrincipal());
        }
        return Optional.empty();
    }
}
