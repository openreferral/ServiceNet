package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Language;

public final class LanguageMother {

    public static final String LANGUAGE = "English";

    public static Language createDefault() {
        return new Language()
            .language(LANGUAGE);
    }

    private LanguageMother() {
    }
}
