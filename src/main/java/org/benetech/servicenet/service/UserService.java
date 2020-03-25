package org.benetech.servicenet.service;

import java.util.Collections;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.ShelterRepository;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.security.SecurityUtils;
import org.benetech.servicenet.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String SYSTEM = "system";

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private CacheManager cacheManager;

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userProfileRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase(Locale.ROOT));
                user.setSystemAccount(getSystemAccount(userDTO));
                user.setShelters(sheltersFromUUIDs(userDTO.getShelters()));
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public Optional<UserProfile> getSystemUser() {
        return userProfileRepository.findOneByLogin(SYSTEM);
    }

    public UserProfile getCurrentOrSystemUser() {
        Optional<UserProfile> current = SecurityUtils.getCurrentUserLogin()
            .flatMap(userProfileRepository::findOneWithAccountByLogin);
        if (current.isPresent()) {
            return current.get();
        }
        Optional<UserProfile> system = userProfileRepository.findOneByLogin(SYSTEM);
        if (system.isPresent()) {
            return system.get();
        }
        throw new IllegalStateException("No current or system user found");
    }

    @Transactional(readOnly = true)
    public Optional<SystemAccount> getCurrentSystemAccount() {
        Optional<UserProfile> current = SecurityUtils.getCurrentUserLogin().flatMap(
            userProfileRepository::findOneByLogin);
         UserProfile userProfile = current.orElseThrow(() -> new IllegalStateException("No current user found"));

        return (userProfile.getSystemAccount() != null) ? Optional.of(userProfile.getSystemAccount()) :
            Optional.empty();
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userProfileRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    public String getCurrentSystemAccountName() {
        Optional<SystemAccount> accountOpt = getCurrentSystemAccount();
        return accountOpt.map(SystemAccount::getName).orElse(null);
    }

    public Optional<UserProfile> getCurrentUserOptional() {
        return SecurityUtils.getCurrentUserLogin().flatMap(
            userProfileRepository::findOneByLogin);
    }

    public Optional<UserProfile> getUser(UUID id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile getCurrentUser() {
        Optional<UserProfile> current = SecurityUtils.getCurrentUserLogin().flatMap(
            userProfileRepository::findOneByLogin);
        return current.orElseThrow(() -> new IllegalStateException("No current user found"));
    }

    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public Boolean isCurrentUserAdmin() {
       return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    }

    private void clearUserCaches(UserProfile userProfile) {
        Cache login = cacheManager.getCache(UserProfileRepository.USERS_BY_LOGIN_CACHE);
        if (login != null) {
            login.evict(userProfile.getLogin());
        } else {
            throw new IllegalStateException("No login cache found!");
        }
    }

    private SystemAccount getSystemAccount(UserDTO userDTO) {
        UUID systemAccountId = userDTO.getSystemAccountId();
        if (systemAccountId != null) {
            return systemAccountRepository.findById(systemAccountId).orElse(null);
        }
        return null;
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
