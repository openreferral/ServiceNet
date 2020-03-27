package org.benetech.servicenet.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.ActivityFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ActivityFilter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityFilterRepository extends JpaRepository<ActivityFilter, UUID> {

    @Query("select activityFilter from ActivityFilter activityFilter"
        + " where activityFilter.userProfile.login = ?#{(principal.getClass().getName().equals(\"java.lang.String\")) ? principal : principal.username} order by name")
    List<ActivityFilter> findByUserIsCurrentUser();

    @Query("select activityFilter from ActivityFilter activityFilter"
        + " where activityFilter.userProfile.login = ?#{(principal.getClass().getName().equals(\"java.lang.String\")) ? principal : principal.username} and activityFilter.name = :name")
    Optional<ActivityFilter> findByNameAndCurrentUser(@Param("name") String name);
}
