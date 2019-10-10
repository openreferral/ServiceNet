package org.benetech.servicenet.repository;

import java.util.List;
import org.benetech.servicenet.domain.Taxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxonomyRepositoryCustom {
    List<Taxonomy> findAssociatedTaxonomies();

    Page<Taxonomy> findAssociatedTaxonomies(Pageable pageable);
}
