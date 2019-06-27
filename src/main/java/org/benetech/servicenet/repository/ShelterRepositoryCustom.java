package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.service.dto.ShelterFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelterRepositoryCustom {
    Page<Shelter> search(ShelterFiltersDTO filters, Pageable pageable);
}
