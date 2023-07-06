package ee.helmes.hotel.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Role {

    private final String HAS_ROLE_PREFIX = "hasRole('ROLE_";
    private final String HAS_ROLE_SUFFIX = "')";
    private final String DELIMITER = " or ";

    public final String HAS_ROLE_USER = HAS_ROLE_PREFIX + AuthoritiesConstants.USER + HAS_ROLE_SUFFIX;
    public final String HAS_ROLE_ADMIN = HAS_ROLE_PREFIX + AuthoritiesConstants.ADMIN + HAS_ROLE_SUFFIX;
    public final String HAS_ROLE_FROM_USER = HAS_ROLE_USER + DELIMITER + HAS_ROLE_ADMIN;
}
