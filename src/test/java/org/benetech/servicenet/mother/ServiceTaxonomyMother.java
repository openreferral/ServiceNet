package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.ServiceTaxonomy;

public final class ServiceTaxonomyMother {

    public static final String DEFAULT_TAXONOMY_DETAILS = "Service taxonomy details";
    public static final String DIFFERENT_TAXONOMY_DETAILS = "Different Service taxonomy details";

    public static ServiceTaxonomy createDefault() {
        return new ServiceTaxonomy()
            .taxonomyDetails(DEFAULT_TAXONOMY_DETAILS);
    }

    public static ServiceTaxonomy createDifferent() {
        return new ServiceTaxonomy()
            .taxonomyDetails(DIFFERENT_TAXONOMY_DETAILS);
    }
}
