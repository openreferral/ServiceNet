package org.benetech.servicenet.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Authority;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.repository.AuthorityRepository;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.repository.PersistentTokenRepository;
import org.benetech.servicenet.repository.ShelterRepository;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.repository.UserRepository;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.security.SecurityUtils;
import org.benetech.servicenet.service.dto.UserDTO;
import org.benetech.servicenet.service.util.RandomUtil;
import org.benetech.servicenet.errors.EmailAlreadyUsedException;
import org.benetech.servicenet.errors.InvalidPasswordException;
import org.benetech.servicenet.errors.LoginAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private CacheManager cacheManager;

    private static final int DAY_IN_SECONDS = 86400;

    private static final int MAX_INACTIVE_PERIOD_IN_DAYS = 3;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(DAY_IN_SECONDS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase(Locale.ROOT)).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase(Locale.ROOT));
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase(Locale.ROOT));
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        newUser.setSystemAccount(getSystemAccount(userDTO));
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase(Locale.ROOT));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase(Locale.ROOT));
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        user.setSystemAccount(getSystemAccount(userDTO));
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName  last name of user
     * @param email     email id of user
     * @param langKey   language key
     * @param imageUrl  image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase(Locale.ROOT));
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase(Locale.ROOT));
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase(Locale.ROOT));
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                user.setSystemAccount(getSystemAccount(userDTO));
                user.setShelters(sheltersFromUUIDs(userDTO.getShelters()));
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        Optional<User> system = getSystemUser();
        if (system.isPresent()) {
            userRepository.findOneByLogin(login).ifPresent(user -> {
                documentUploadRepository.findAllByUploaderId(user.getId()).forEach(doc -> {
                        doc.setUploader(system.get());
                        documentUploadRepository.save(doc);
                    });

                metadataRepository.findAllByUserId(user.getId()).forEach(meta -> {
                        meta.setUser(system.get());
                        metadataRepository.save(meta);
                    });

                userRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
        } else {
            throw new IllegalStateException("User's metadata couldn't be archived.");
        }
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesAndAccountByLogin(String login) {
        return userRepository.findOneWithAuthoritiesAndAccountByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesAndAccount(UUID id) {
        return userRepository.findOneWithAuthoritiesAndAccountById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesAndAccount() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesAndAccountByLogin);
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after MAX_INACTIVE_PERIOD_IN_DAYS
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(MAX_INACTIVE_PERIOD_IN_DAYS, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public Optional<User> getSystemUser() {
        return userRepository.findOneByLogin(SYSTEM);
    }

    public User getCurrentOrSystemUser() {
        Optional<User> current = getUserWithAuthoritiesAndAccount();
        if (current.isPresent()) {
            return current.get();
        }
        Optional<User> system = userRepository.findOneByLogin(SYSTEM);
        if (system.isPresent()) {
            return system.get();
        }
        throw new IllegalStateException("No current or system user found");
    }

    /**
     * @return true if any user is logged, and he has admin role
     */
    public boolean isCurrentUserAdmin() {
        return getUserWithAuthoritiesAndAccount().map(User::isAdmin)
            .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<SystemAccount> getCurrentSystemAccount() {
        Optional<User> current = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
         User user = current.orElseThrow(() -> new IllegalStateException("No current user found"));

        return (user.getSystemAccount() != null) ? Optional.of(user.getSystemAccount()) :
            Optional.empty();
    }

    public String getCurrentSystemAccountName() {
        Optional<SystemAccount> accountOpt = getCurrentSystemAccount();
        return accountOpt.map(SystemAccount::getName).orElse(null);
    }

    public User getCurrentUser() {
        Optional<User> current = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        return current.orElseThrow(() -> new IllegalStateException("No current user found"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    private static User getUser(Map<String, Object> details) {
        User user = new User();
        // handle resource server JWT, where sub claim is email and uid is ID
        if (details.get("uid") != null) {
            user.setId((UUID) details.get("uid"));
            user.setLogin((String) details.get("sub"));
        } else {
            user.setId((UUID) details.get("sub"));
        }
        if (details.get("preferred_username") != null) {
            user.setLogin(((String) details.get("preferred_username")).toLowerCase());
        } else if (user.getLogin() == null) {
            user.setLogin(user.getId().toString());
        }
        if (details.get("given_name") != null) {
            user.setFirstName((String) details.get("given_name"));
        }
        if (details.get("family_name") != null) {
            user.setLastName((String) details.get("family_name"));
        }
        if (details.get("email_verified") != null) {
            user.setActivated((Boolean) details.get("email_verified"));
        }
        if (details.get("email") != null) {
            user.setEmail(((String) details.get("email")).toLowerCase());
        } else {
            user.setEmail((String) details.get("sub"));
        }
        if (details.get("langKey") != null) {
            user.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            // trim off country code if it exists
            String locale = (String) details.get("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            user.setLangKey(locale.toLowerCase());
        } else {
            // set langKey to default if not specified by IdP
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        if (details.get("picture") != null) {
            user.setImageUrl((String) details.get("picture"));
        }
        user.setActivated(true);
        return user;
    }

    private void clearUserCaches(User user) {
        Cache login = cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE);
        Cache email = cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE);
        if (login != null && email != null) {
            login.evict(user.getLogin());
            email.evict(user.getEmail());
        } else {
            throw new IllegalStateException("No login/email cache found!");
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

    private User syncUserWithIdP(Map<String, Object> details, User user) {
        // save authorities in to sync user roles/groups between IdP and JHipster's local database
        Collection<String> dbAuthorities = getAuthorities();
        Collection<String> userAuthorities =
            user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        for (String authority : userAuthorities) {
            if (!dbAuthorities.contains(authority)) {
                log.debug("Saving authority '{}' in local database", authority);
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            }
        }
        // save account in to sync users between IdP and JHipster's local database
        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            // if IdP sends last updated information, use it to determine if an update should happen
            if (details.get("updated_at") != null) {
                Instant dbModifiedDate = existingUser.get().getLastModifiedDate();
                Instant idpModifiedDate = (Instant) details.get("updated_at");
                if (idpModifiedDate.isAfter(dbModifiedDate)) {
                    log.debug("Updating user '{}' in local database", user.getLogin());
                    updateUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                        user.getLangKey(), user.getImageUrl());
                }
                // no last updated info, blindly update
            } else {
                log.debug("Updating user '{}' in local database", user.getLogin());
                updateUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getLangKey(), user.getImageUrl());
            }
        } else {
            log.debug("Saving user '{}' in local database", user.getLogin());
            userRepository.save(user);
            this.clearUserCaches(user);
        }
        return user;
    }
}
