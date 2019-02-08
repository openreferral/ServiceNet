package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Taxonomy;

public interface TaxonomyImportService {

    Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName);
}
