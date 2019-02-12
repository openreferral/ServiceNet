package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = { "authorities", "systemAccount" })
    Optional<User> findOneWithAuthoritiesAndAccountById(UUID id);

    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    @EntityGraph(attributePaths = { "authorities", "systemAccount" })
    Optional<User> findOneWithAuthoritiesAndAccountByLogin(String login);

    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    @EntityGraph(attributePaths = { "authorities", "systemAccount" })
    Optional<User> findOneWithAuthoritiesAndAccountByEmail(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);
}
