package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.OpeningHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the OpeningHours entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, UUID> {

    Page<OpeningHours> findAll(Pageable pageable);
}
