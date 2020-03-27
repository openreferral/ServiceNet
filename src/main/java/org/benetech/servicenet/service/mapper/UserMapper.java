package org.benetech.servicenet.service.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.ShelterRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mapper for the entity User and its DTO called UserDTO.
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    private SystemAccountMapper systemAccountMapper;

    private ShelterRepository shelterRepository;

    private UserService userService;

    public UserMapper(SystemAccountMapper systemAccountMapper, ShelterRepository shelterRepository,
        UserService userService) {
        this.userService = userService;
        this.systemAccountMapper = systemAccountMapper;
        this.shelterRepository = shelterRepository;
    }

    public UserDTO userToUserDTO(UserProfile userProfile) {
        return new UserDTO(userProfile);
    }

    public List<UserDTO> usersToUserDTOs(List<UserProfile> userProfiles) {
        return userProfiles.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public UserProfile userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            UserProfile userProfile = new UserProfile();
            UserProfile existingProfile = userService.getOrCreateUserProfile(userDTO.getId(), userDTO.getLogin());
            userProfile.setId(existingProfile.getId());
            userProfile.setUserId(userDTO.getId());
            userProfile.setLogin(userDTO.getLogin());
            userProfile.setShelters(this.sheltersFromUUIDs(userDTO.getShelters()));
            userProfile.setSystemAccount(systemAccountMapper.fromId(userDTO.getSystemAccountId()));
            return userProfile;
        }
    }

    public List<UserProfile> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }

    public UserProfile userFromId(UUID id) {
        if (id == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        return userProfile;
    }

    private Set<Shelter> sheltersFromUUIDs(List<UUID> uuids) {
        if (uuids != null) {
            return uuids.stream()
                .map(uuid -> shelterRepository.getOne(uuid))
                .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }
}
