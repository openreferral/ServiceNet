package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.MatchSimilarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MatchSimilarity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatchSimilarityRepository extends JpaRepository<MatchSimilarity, UUID> {

    Page<MatchSimilarity> findAll(Pageable pageable);
}
