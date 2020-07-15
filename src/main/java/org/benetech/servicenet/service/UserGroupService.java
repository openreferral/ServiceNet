package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.UserGroupDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.UserGroup}.
 */
public interface UserGroupService {

    /**
     * Save a userGroup.
     *
     * @param userGroupDTO the entity to save.
     * @return the persisted entity.
     */
    UserGroupDTO save(UserGroupDTO userGroupDTO);

    /**
     * Get all the userGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserGroupDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserGroupDTO> findOne(UUID id);

    Optional<UserGroupDTO> findOneByName(String name);

    /**
     * Delete the "id" userGroup.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
