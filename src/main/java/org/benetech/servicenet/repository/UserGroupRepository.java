package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.UserGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Spring Data  repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

    Optional<UserGroup> getByName(String name);
}
