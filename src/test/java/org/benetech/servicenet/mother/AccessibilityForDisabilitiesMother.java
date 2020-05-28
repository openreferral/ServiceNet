package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class AccessibilityForDisabilitiesMother {

    public static final String DEFAULT_DETAILS = "Accessibility Details";
    public static final String DIFFERENT_DETAILS = "Different Accessibility Details";
    public static final String DEFAULT_ACCESSIBILITY = "Accessibility";
    public static final String DIFFERENT_ACCESSIBILITY = "Different Accessibility";

    public static AccessibilityForDisabilities createDefault() {
        return new AccessibilityForDisabilities()
            .details(DEFAULT_DETAILS)
            .accessibility(DEFAULT_ACCESSIBILITY);
    }

    public static AccessibilityForDisabilities createDifferent() {
        return new AccessibilityForDisabilities()
            .details(DIFFERENT_DETAILS)
            .accessibility(DIFFERENT_ACCESSIBILITY);
    }
}
