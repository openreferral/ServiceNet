package org.benetech.servicenet.service.mapper;

import java.util.Collections;
import java.util.HashSet;
import org.benetech.servicenet.domain.Authority;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.repository.ShelterRepository;
import org.benetech.servicenet.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    public UserMapper(SystemAccountMapper systemAccountMapper, ShelterRepository shelterRepository) {
        this.systemAccountMapper = systemAccountMapper;
        this.shelterRepository = shelterRepository;
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            user.setShelters(this.sheltersFromUUIDs(userDTO.getShelters()));
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            if (authorities != null) {
                user.setAuthorities(authorities);
            }
            user.setSystemAccount(systemAccountMapper.fromId(userDTO.getSystemAccountId()));
            return user;
        }
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }

    public User userFromId(UUID id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    private Set<Authority> authoritiesFromStrings(Set<String> strings) {
        return (strings != null) ?strings.stream().map(string -> {
            Authority auth = new Authority();
            auth.setName(string);
            return auth;
        }).collect(Collectors.toSet()) : new HashSet<>();
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
