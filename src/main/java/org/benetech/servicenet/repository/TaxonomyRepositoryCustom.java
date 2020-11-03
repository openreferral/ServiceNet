package org.benetech.servicenet.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxonomyRepositoryCustom {
    List<Taxonomy> findAssociatedTaxonomies();

    List<Taxonomy> findAssociatedTaxonomies(UUID siloId, String providerName, UserProfile excludedUser);

    List<Taxonomy> findAssociatedTaxonomies(Set<Organization> organizations);

    Page<Taxonomy> findAssociatedTaxonomies(Pageable pageable);
}
