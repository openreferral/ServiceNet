package org.benetech.servicenet.service.util;

import java.security.SecureRandom;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private RandomUtil() {
    }

    private static String generate(boolean letters) {
        return RandomStringUtils.random(DEF_COUNT, 0, 0, letters,
            true, null, new SecureRandom());
    }

    /**
     * Generate a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return generate(true);
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return generate(false);
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key
     */
    public static String generateResetKey() {
        return generate(false);
    }

    /**
     * Generate a unique series to validate a persistent token, used in the
     * authentication remember-me mechanism.
     *
     * @return the generated series data
     */
    public static String generateSeriesData() {
        return generate(true);
    }

    /**
     * Generate a persistent token, used in the authentication remember-me mechanism.
     *
     * @return the generated token data
     */
    public static String generateTokenData() {
        return generate(true);
    }
}
