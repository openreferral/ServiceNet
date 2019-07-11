package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

@Component
public class TaxonomyImportServiceImpl implements TaxonomyImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private TaxonomyService taxonomyService;

    @Override
    public Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName,
                                           DataImportReport report) {
        if (taxonomy == null) {
            return null;
        }
        EntityValidator.validateAndFix(taxonomy, null, report, externalDbId);

        Optional<Taxonomy> taxonomyFromDb = taxonomyService.findForExternalDb(externalDbId, providerName);

        if (taxonomyFromDb.isPresent()) {
            taxonomy.setId(taxonomyFromDb.get().getId());
            return em.merge(taxonomy);
        } else {
            em.persist(taxonomy);
            return taxonomy;
        }
    }
}
