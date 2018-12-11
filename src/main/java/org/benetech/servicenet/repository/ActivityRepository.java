package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the Activity entity.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    @Query("select activity from Activity activity where activity.user.login = ?#{principal.username}")
    List<Activity> findByUserIsCurrentUser();

    @Query(value = "select distinct activity from Activity activity left join fetch activity.reviewers",
        countQuery = "select count(distinct activity) from Activity activity")
    Page<Activity> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct activity from Activity activity left join fetch activity.reviewers")
    List<Activity> findAllWithEagerRelationships();

    @Query("select activity from Activity activity left join fetch activity.reviewers where activity.id =:id")
    Optional<Activity> findOneWithEagerRelationships(@Param("id") UUID id);

}
