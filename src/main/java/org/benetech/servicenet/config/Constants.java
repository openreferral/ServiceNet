package org.benetech.servicenet.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String DEFAULT_LANGUAGE = "en";

    public static final String ALL_FIELDS = "ALL FIELDS";

    public static final String CONFLICT_DETECTOR_SUFFIX = "ConflictDetector";

    public static final String UWBA_PROVIDER = "UWBA";

    public static final String EDEN_PROVIDER = "Eden";

    public static final String SHELTER_TECH_PROVIDER = "ShelterTech";

    public static final String LAAC_PROVIDER = "LAAC";

    public static final String SMC_CONNECT_PROVIDER = "SMCConnect";

    public static final String HEALTHLEADS_PROVIDER = "healthleads";

    public static final String SERVICE_PROVIDER = "ServiceProvider";

    public static final String SPRING_PROFILE_STAGING = "staging";

    private Constants() {
    }
}
