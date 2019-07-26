package org.benetech.servicenet.service.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        Optional<Taxonomy> taxonomyFromDb = findExistingTaxonomy(externalDbId, taxonomy.getTaxonomyId(), providerName);

        if (taxonomyFromDb.isPresent()) {
            fillDataFromDb(taxonomy, taxonomyFromDb.get());
            return em.merge(taxonomy);
        } else {
            em.persist(taxonomy);
            return taxonomy;
        }
    }

    @Override
    @Transactional
    public void importTaxonomies(List<Taxonomy> taxonomies) {
        taxonomies.forEach(taxonomy -> {
            Optional<Taxonomy> taxonomyFromDb =
                findExistingTaxonomy(taxonomy.getExternalDbId(), taxonomy.getTaxonomyId(), taxonomy.getProviderName());

            Taxonomy parent = taxonomy.getParent();
            if (parent != null) {
                Optional<Taxonomy> parentFromDb =
                    findExistingTaxonomy(parent.getExternalDbId(), parent.getTaxonomyId(), parent.getProviderName());

                parentFromDb.ifPresentOrElse(taxonomy::setParent, () -> em.persist(parent));
            }

            if (taxonomyFromDb.isPresent()) {
                taxonomy.setId(taxonomyFromDb.get().getId());
                em.merge(taxonomy);
            } else {
                em.persist(taxonomy);
            }
        });
    }

    private void fillDataFromDb(Taxonomy newTaxonomy, Taxonomy taxonomyFromDb) {
        newTaxonomy.setId(taxonomyFromDb.getId());
        newTaxonomy.setParent(taxonomyFromDb.getParent());

        if (StringUtils.isBlank(newTaxonomy.getExternalDbId())) {
            newTaxonomy.setExternalDbId(taxonomyFromDb.getExternalDbId());
        }

        if (StringUtils.isBlank(newTaxonomy.getTaxonomyId())) {
            newTaxonomy.setTaxonomyId(taxonomyFromDb.getTaxonomyId());
        }

        if (StringUtils.isBlank(newTaxonomy.getName())) {
            newTaxonomy.setName(taxonomyFromDb.getName());
        }

        if (StringUtils.isBlank(newTaxonomy.getDetails())) {
            newTaxonomy.setDetails(taxonomyFromDb.getDetails());
        }
    }

    private Optional<Taxonomy> findExistingTaxonomy(String externalDbId, String taxonomyId, String providerName) {
        if (StringUtils.isNotBlank(externalDbId)) {
            return taxonomyService.findForExternalDb(externalDbId, providerName);
        } else if (StringUtils.isNotBlank(taxonomyId)) {
            return taxonomyService.findForTaxonomyId(taxonomyId, providerName);
        }

        return Optional.empty();
    }
}
