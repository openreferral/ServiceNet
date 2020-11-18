package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Taxonomy;

public final class TaxonomyMother {

    public static final String TAXONOMY = "taxonomy";

    public static Taxonomy createDefault() {
        return new Taxonomy()
            .name(TAXONOMY);
    }

    private TaxonomyMother() {
    }
}
