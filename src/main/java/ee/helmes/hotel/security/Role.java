package ee.helmes.hotel.security;

public class Role {

    private static final String HAS_ROLE_PREFIX = "hasRole('";
    private static final String HAS_ROLE_SUFFIX = "')";
    private static final String DELIMITER = " or ";

    public static final String HAS_ROLE_USER = HAS_ROLE_PREFIX + AuthoritiesConstants.USER + HAS_ROLE_SUFFIX;
    public static final String HAS_ROLE_ADMIN = HAS_ROLE_PREFIX + AuthoritiesConstants.ADMIN + HAS_ROLE_SUFFIX;
    public static final String HAS_ROLE_FROM_USER = HAS_ROLE_USER + DELIMITER + HAS_ROLE_ADMIN;
}
