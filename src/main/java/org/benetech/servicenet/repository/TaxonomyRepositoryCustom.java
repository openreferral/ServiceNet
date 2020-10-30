package org.benetech.servicenet.repository;

import java.util.List;
import java.util.Set;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Taxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxonomyRepositoryCustom {
    List<Taxonomy> findAssociatedTaxonomies();

    List<Taxonomy> findAssociatedTaxonomies(String providerName);

    List<Taxonomy> findAssociatedTaxonomies(Set<Organization> organizations);

    Page<Taxonomy> findAssociatedTaxonomies(Pageable pageable);
}
