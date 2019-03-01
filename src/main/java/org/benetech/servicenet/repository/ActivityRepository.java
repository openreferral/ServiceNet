package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.view.ActivityInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityInfo, UUID> {

    @Query(value =
        "SELECT * FROM activity_info\n" +
        "WHERE ACCOUNT_ID = :ownerId",
        countQuery = "SELECT COUNT(ID)\n" +
                     "FROM ORGANIZATION ORG\n" +
                     "WHERE ORG.ACCOUNT_ID = :ownerId",
        nativeQuery = true)
    Page<ActivityInfo> findAllOrgIdsWithOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);

    @Query(value =
        "SELECT * FROM activity_info\n" +
            "WHERE ACCOUNT_ID = :ownerId AND UPPER(NAME) LIKE UPPER(:search)",
        countQuery = "SELECT COUNT(ID)\n" +
            "FROM ORGANIZATION ORG\n" +
            "WHERE ORG.ACCOUNT_ID = :ownerId AND UPPER(NAME) LIKE UPPER(:search)",
        nativeQuery = true)
    Page<ActivityInfo> findAllOrgIdsWithOwnerIdAndSearchPhrase(@Param("ownerId") UUID ownerId,
                                                               @Param("search") String search, Pageable pageable);
}
