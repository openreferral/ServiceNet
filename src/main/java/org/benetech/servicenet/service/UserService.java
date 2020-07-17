package org.benetech.servicenet.service;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import org.benetech.servicenet.client.ServiceNetAuthClient;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserGroup;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.errors.HystrixBadRequestAlertException;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.repository.ShelterRepository;
import org.benetech.servicenet.repository.SiloRepository;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.repository.UserGroupRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.security.SecurityUtils;
import org.benetech.servicenet.service.dto.OwnerDTO;
import org.benetech.servicenet.service.dto.UserDTO;
import org.benetech.servicenet.service.dto.UserRegisterDTO;
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

    @Autowired
    private ServiceNetAuthClient authClient;

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private SiloRepository siloRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    /**
     * Create a new user.
     *
     * @param userDTO user to create
     * @return created user
     */
    public UserDTO createUser(UserDTO userDTO) {
        try {
            return createOrUpdateUserProfile(
                authClient.createUser(userDTO), userDTO
            );
        } catch (HystrixBadRequestException e) {
            handleHystrixException(e);
            return null;
        }
    }

    /**
     * Register a new user.
     *
     * @param userRegisterDTO user to register
     * @return registered user
     */
    public UserDTO registerUser(UserRegisterDTO userRegisterDTO) {
        try {
            return createOrUpdateUserProfile(
                authClient.registerUser(userRegisterDTO), userRegisterDTO
            );
        } catch (HystrixBadRequestException e) {
            handleHystrixException(e);
            return null;
        }
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public UserDTO updateUser(UserDTO userDTO) {
        try {
            return createOrUpdateUserProfile(
                authClient.updateUser(userDTO), userDTO
            );
        } catch (HystrixBadRequestException e) {
            handleHystrixException(e);
            return null;
        }
    }

    public void deleteUser(String login) {
        Optional<UserProfile> system = getSystemUserProfile();
        if (system.isPresent()) {
            userProfileRepository.findOneByLogin(login).ifPresent(user -> {
                authClient.deleteUser(login);
                documentUploadRepository.findAllByUploaderId(user.getId()).forEach(doc -> {
                    doc.setUploader(system.get());
                    documentUploadRepository.save(doc);
                });
                metadataRepository.findAllByUserId(user.getId()).forEach(meta -> {
                    meta.setUserProfile(system.get());
                    metadataRepository.save(meta);
                });
                userProfileRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
        } else {
            throw new IllegalStateException("User's metadata couldn't be archived.");
        }
    }

    public UserDTO getUser(String login) {
        UserDTO userDTO = authClient.getUser(login);
        return getCompleteUserDto(userDTO, getOrCreateUserProfile(userDTO.getId(), login));
    }

    public UserDTO getAccount() {
        UserProfile currentProfile = getCurrentUserProfile();
        UserDTO userDTO = authClient.getUser(currentProfile.getLogin());
        return getCompleteUserDto(userDTO, currentProfile);
    }

    public List<String> getAuthorities() {
        return authClient.getAuthorities();
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return authClient.getUsers(pageable).map(u -> {
            final Optional<UserProfile> optUserProfile = userProfileRepository.findOneByLogin(u.getLogin());
            if (optUserProfile.isPresent()) {
                UserProfile userProfile = optUserProfile.get();
                SystemAccount systemAccount = userProfile.getSystemAccount();
                if (systemAccount != null) {
                    u.setSystemAccountId(systemAccount.getId());
                    u.setSystemAccountName(systemAccount.getName());
                }
                if (userProfile.getSilo() != null) {
                    u.setSiloId(userProfile.getSilo().getId());
                }
                u.setPhoneNumber(userProfile.getPhoneNumber());
                u.setOrganizationName(userProfile.getOrganizationName());
                u.setOrganizationUrl(userProfile.getOrganizationUrl());
            }
            return u;
        });
    }

    @Transactional(readOnly = true)
    public Page<UserProfile> getAllManagedUserProfiles(Pageable pageable) {
        return userProfileRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER);
    }

    public Optional<UserProfile> getSystemUserProfile() {
        return userProfileRepository.findOneByLogin(SYSTEM);
    }

    public UserProfile getCurrentOrSystemUserProfile() {
        Optional<UserProfile> userProfile = getCurrentUserProfileOptional();
        if (userProfile.isPresent()) {
            return userProfile.get();
        } else {
            Optional<UserProfile> system = userProfileRepository.findOneByLogin(SYSTEM);
            if (system.isPresent()) {
                return system.get();
            }
            throw new IllegalStateException("No current or system user found");
        }
    }

    @Transactional(readOnly = true)
    public Optional<SystemAccount> getCurrentSystemAccount() {
        UserProfile userProfile = getCurrentUserProfile();
        return (userProfile.getSystemAccount() != null) ? Optional.of(userProfile.getSystemAccount()) :
            Optional.empty();
    }

    public String getCurrentSystemAccountName() {
        Optional<SystemAccount> accountOpt = getCurrentSystemAccount();
        return accountOpt.map(SystemAccount::getName).orElse(null);
    }

    public Optional<UserProfile> getCurrentUserProfileOptional() {
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isPresent()) {
            UUID userId = SecurityUtils.getCurrentUserId();
            return Optional.of(getOrCreateUserProfile(userId, login.get()));
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserProfile> getUserProfile(UUID id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile getCurrentUserProfile() {
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isPresent()) {
            UUID userId = SecurityUtils.getCurrentUserId();
            return getOrCreateUserProfile(userId, login.get());
        } else {
            throw new IllegalStateException("No current user found");
        }
    }

    public UserProfile saveProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public Boolean isCurrentUserAdmin() {
       return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    }

    public UserProfile getOrCreateUserProfile(UUID userId, String login) {
        Optional<UserProfile> existingProfile = userProfileRepository
            .findOneByLogin(login.toLowerCase(Locale.ROOT));
        if (existingProfile.isPresent()) {
            return existingProfile.get();
        } else {
            existingProfile = userProfileRepository.findOneByLogin(login);
            UserProfile userProfile = new UserProfile();
            if (existingProfile.isPresent()) {
                userProfile = existingProfile.get();
            }
            userProfile.setUserId(userId);
            userProfile.setLogin(login);
            SystemAccount systemAccount = systemAccountRepository
                .findByName(Constants.SERVICE_PROVIDER).orElse(null);
            userProfile.setSystemAccount(systemAccount);
            return userProfileRepository.save(userProfile);
        }
    }

    public UserDTO getCompleteUserDto(UserDTO authUser, UserProfile userProfile) {
        SystemAccount systemAccount = userProfile.getSystemAccount();
        if (systemAccount != null) {
            authUser.setSystemAccountId(systemAccount.getId());
            authUser.setSystemAccountName(systemAccount.getName());
        }
        if (userProfile.getShelters() != null) {
            authUser.setShelters(userProfile.getShelters().stream()
                .map(Shelter::getId).collect(Collectors.toList()));
        }
        if (userProfile.getSilo() != null) {
            authUser.setSiloId(userProfile.getSilo().getId());
        }
        authUser.setOrganizationName(userProfile.getOrganizationName());
        authUser.setOrganizationUrl(userProfile.getOrganizationUrl());
        authUser.setPhoneNumber(userProfile.getPhoneNumber());
        if (userProfile.getUserGroups() != null) {
            authUser.setUserGroups(userProfile.getUserGroups().stream()
                .map(UserGroup::getId)
                .collect(Collectors.toList()));
        }
        return authUser;
    }

    public OwnerDTO getUserDtoOfOrganization(Organization organization) {
        OwnerDTO result = null;
        if (organization != null) {
            Set<UserProfile> userProfiles = organization.getUserProfiles();
            if (userProfiles.size() > 0) {
                UserProfile userProfile = new ArrayList<UserProfile>(userProfiles).get(userProfiles.size() - 1);
                UserDTO userDTO = this.getUser(userProfile.getLogin());
                result = this.userDtoToOwnerDto(userDTO);
            }
        }

        return result;
    }

    private OwnerDTO userDtoToOwnerDto(UserDTO userDTO) {
        OwnerDTO result = new OwnerDTO();
        result.setFirstName(userDTO.getFirstName());
        result.setLastName(userDTO.getLastName());
        result.setEmail(userDTO.getEmail());
        return result;
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
        return systemAccountRepository.findByName(Constants.SERVICE_PROVIDER).orElse(null);
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

    private Set<UserGroup> userGroupsFromUUIDs(List<UUID> uuids) {
        if (uuids != null) {
            return uuids.stream()
                .filter(Objects::nonNull)
                .map(uuid -> userGroupRepository.getOne(uuid))
                .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    private Set<Organization> organizationsFromUUIDs(List<UUID> uuids) {
        if (uuids != null) {
            return uuids.stream()
                .map(uuid -> organizationRepository.getOne(uuid))
                .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    private UserDTO createOrUpdateUserProfile(UserDTO authUser, UserDTO userDTO) {
        UserProfile userProfile = getOrCreateUserProfile(authUser.getId(), userDTO.getLogin());
        userProfile.setLogin(userDTO.getLogin().toLowerCase(Locale.ROOT));
        userProfile.setOrganizationName(userDTO.getOrganizationName());
        userProfile.setOrganizationUrl(userDTO.getOrganizationUrl());
        userProfile.setPhoneNumber(userDTO.getPhoneNumber());
        userProfile.setSystemAccount(getSystemAccount(userDTO));
        userProfile.setShelters(sheltersFromUUIDs(userDTO.getShelters()));
        userProfile.setSilo(this.getSilo(userDTO.getSiloId()));
        userProfile.setUserGroups(userGroupsFromUUIDs(userDTO.getUserGroups()));
        userProfileRepository.save(userProfile);
        this.clearUserCaches(userProfile);
        return getCompleteUserDto(authUser, userProfile);
    }

    private Silo getSilo(UUID id) {
        if (id == null) {
            return null;
        }
        Optional<Silo> silo = siloRepository.findById(id);
        return silo.orElse(null);
    }

    private void handleHystrixException(HystrixBadRequestException e) {
        if (e instanceof HystrixBadRequestAlertException) {
            throw ((HystrixBadRequestAlertException) e).getCause();
        }
        throw e;
    }
}
