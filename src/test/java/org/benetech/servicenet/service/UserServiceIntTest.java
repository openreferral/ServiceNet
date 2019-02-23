package org.benetech.servicenet.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.PersistentToken;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.listener.HibernatePostCreateListener;
import org.benetech.servicenet.listener.HibernatePostDeleteListener;
import org.benetech.servicenet.listener.HibernatePostUpdateListener;
import org.benetech.servicenet.mother.DocumentUploadMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.mother.UserMother;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.repository.PersistentTokenRepository;
import org.benetech.servicenet.repository.UserRepository;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.dto.UserDTO;
import org.benetech.servicenet.service.util.RandomUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ServiceNetApp.class, MockedUserTestConfiguration.class })
@Transactional
public class UserServiceIntTest {

    @Mock
    DateTimeProvider dateTimeProvider;

    @Mock
    private MetadataService metadataService;

    @Autowired
    @InjectMocks
    private HibernatePostUpdateListener hibernatePostUpdateListener;

    @Autowired
    @InjectMocks
    private HibernatePostCreateListener hibernatePostCreateListener;

    @Autowired
    @InjectMocks
    private HibernatePostDeleteListener hibernatePostDeleteListener;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditingHandler auditingHandler;

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @PersistenceContext
    private EntityManager em;

    private User user;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        persistentTokenRepository.deleteAll();
        user = new User();
        user.setLogin("johndoe");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("en");

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }

    @Test
    @Transactional
    public void testRemoveOldPersistentTokens() {
        userRepository.saveAndFlush(user);
        int existingCount = persistentTokenRepository.findByUser(user).size();
        LocalDate today = LocalDate.now();
        generateUserToken(user, "1111-1111", today);
        generateUserToken(user, "2222-2222", today.minusDays(32));
        assertThat(persistentTokenRepository.findByUser(user)).hasSize(existingCount + 2);
        userService.removeOldPersistentTokens();
        assertThat(persistentTokenRepository.findByUser(user)).hasSize(existingCount + 1);
    }

    @Test
    @Transactional
    public void assertThatUserMustExistToResetPassword() {
        userRepository.saveAndFlush(user);
        Optional<User> maybeUser = userService.requestPasswordReset("invalid.login@localhost");
        assertThat(maybeUser).isNotPresent();

        maybeUser = userService.requestPasswordReset(user.getEmail());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getEmail()).isEqualTo(user.getEmail());
        assertThat(maybeUser.orElse(null).getResetDate()).isNotNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNotNull();
    }

    @Test
    @Transactional
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        user.setActivated(false);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.requestPasswordReset(user.getLogin());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatResetKeyMustBeValid() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey("1234");
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatUserCanResetPassword() {
        String oldPassword = user.getPassword();
        Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getResetDate()).isNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNull();
        assertThat(maybeUser.orElse(null).getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void testFindNotActivatedUsersByCreationDateBefore() {
        Instant now = Instant.now();
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
        user.setActivated(false);
        User dbUser = userRepository.saveAndFlush(user);
        dbUser.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
        userRepository.saveAndFlush(user);
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isNotEmpty();
        userService.removeNotActivatedUsers();
        users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isEmpty();
    }

    private void generateUserToken(User user, String tokenSeries, LocalDate localDate) {
        PersistentToken token = new PersistentToken();
        token.setSeries(tokenSeries);
        token.setUser(user);
        token.setTokenValue(tokenSeries + "-data");
        token.setTokenDate(localDate);
        token.setIpAddress("127.0.0.1");
        token.setUserAgent("Test agent");
        persistentTokenRepository.saveAndFlush(token);
    }

    @Test
    @Transactional
    public void assertThatAnonymousUserIsNotGet() {
        user.setLogin(Constants.ANONYMOUS_USER);
        if (!userRepository.findOneByLogin(Constants.ANONYMOUS_USER).isPresent()) {
            userRepository.saveAndFlush(user);
        }
        final PageRequest pageable = PageRequest.of(0, (int) userRepository.count());
        final Page<UserDTO> allManagedUsers = userService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent().stream()
            .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin())))
            .isTrue();
    }

    @Test
    @Transactional
    public void testRemoveNotActivatedUsers() {
        // custom "now" for audit to use as creation date
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(Instant.now().minus(30, ChronoUnit.DAYS)));

        user.setActivated(false);
        userRepository.saveAndFlush(user);

        assertThat(userRepository.findOneByLogin("johndoe")).isPresent();
        userService.removeNotActivatedUsers();
        assertThat(userRepository.findOneByLogin("johndoe")).isNotPresent();
    }

    @Test
    @Transactional
    public void shouldFetchUserByIdWithAllSimpleValues() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccount(user.getId());

        assertTrue(fetchedOpt.isPresent());
        User fetched = fetchedOpt.get();
        assertEquals(user.getLogin(), fetched.getLogin());
        assertEquals(user.getPassword(), fetched.getPassword());
        assertEquals(user.getEmail(), fetched.getEmail());
        assertEquals(user.getFirstName(), fetched.getFirstName());
        assertEquals(user.getFirstName(), fetched.getFirstName());
        assertEquals(user.getLastName(), fetched.getLastName());
        assertEquals(user.getImageUrl(), fetched.getImageUrl());
        assertEquals(user.getLangKey(), fetched.getLangKey());
    }

    @Test
    @Transactional
    public void shouldFetchUserByIdWithAuthorities() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccount(user.getId());

        assertTrue(fetchedOpt.isPresent());
        assertFalse(fetchedOpt.get().getAuthorities().isEmpty());
        assertEquals(1, fetchedOpt.get().getAuthorities().size());
        assertTrue(fetchedOpt.get().getAuthorities().stream().anyMatch(
            x -> x.getName().equalsIgnoreCase(AuthoritiesConstants.USER)));
    }

    @Test
    @Transactional
    public void shouldFetchUserByIdWithSystemAccount() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccount(user.getId());

        assertTrue(fetchedOpt.isPresent());
        assertNotNull(fetchedOpt.get().getSystemAccount());
        assertEquals(SystemAccountMother.DEFAULT_NAME, fetchedOpt.get().getSystemAccount().getName());
    }

    @Test
    @Transactional
    public void shouldConvertFetchedUserByIdWithSystemAccount() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccount(user.getId());
        assertTrue(fetchedOpt.isPresent());
        UserDTO userDTO = new UserDTO(fetchedOpt.get());

        assertNotNull(userDTO.getSystemAccountName());
        assertEquals(SystemAccountMother.DEFAULT_NAME, userDTO.getSystemAccountName());
    }

    @Test
    @Transactional
    public void shouldUpdateFetchedUserByIdWithSystemAccount() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccount(user.getId());
        assertTrue(fetchedOpt.isPresent());
        UserDTO userDTO = new UserDTO(fetchedOpt.get());
        SystemAccount account = SystemAccountMother.createDifferentAndPersist(em);
        userDTO.setSystemAccountName(account.getName());
        userDTO.setSystemAccountId(account.getId());

        Optional<UserDTO> updated = userService.updateUser(userDTO);

        assertTrue(updated.isPresent());
        assertNotNull(updated.get().getSystemAccountName());
        assertEquals(SystemAccountMother.UPDATED_NAME, updated.get().getSystemAccountName());
    }

    @Test
    @Transactional
    public void shouldFetchUserByLoginWithAllSimpleValues() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccountByLogin(user.getLogin());

        assertTrue(fetchedOpt.isPresent());
        User fetched = fetchedOpt.get();
        assertEquals(user.getLogin(), fetched.getLogin());
        assertEquals(user.getPassword(), fetched.getPassword());
        assertEquals(user.getEmail(), fetched.getEmail());
        assertEquals(user.getFirstName(), fetched.getFirstName());
        assertEquals(user.getFirstName(), fetched.getFirstName());
        assertEquals(user.getLastName(), fetched.getLastName());
        assertEquals(user.getImageUrl(), fetched.getImageUrl());
        assertEquals(user.getLangKey(), fetched.getLangKey());
    }

    @Test
    @Transactional
    public void shouldFetchUserByLoginWithAuthorities() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccountByLogin(user.getLogin());

        assertTrue(fetchedOpt.isPresent());
        assertFalse(fetchedOpt.get().getAuthorities().isEmpty());
        assertEquals(1, fetchedOpt.get().getAuthorities().size());
        assertTrue(fetchedOpt.get().getAuthorities().stream().anyMatch(
            x -> x.getName().equalsIgnoreCase(AuthoritiesConstants.USER)));
    }

    @Test
    @Transactional
    public void shouldFetchUserByLoginWithSystemAccount() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccountByLogin(user.getLogin());

        assertTrue(fetchedOpt.isPresent());
        assertNotNull(fetchedOpt.get().getSystemAccount());
        assertEquals(SystemAccountMother.DEFAULT_NAME, fetchedOpt.get().getSystemAccount().getName());
    }

    @Test
    @Transactional
    public void shouldConvertFetchedUserByLoginWithSystemAccount() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccountByLogin(user.getLogin());
        assertTrue(fetchedOpt.isPresent());
        UserDTO userDTO = new UserDTO(fetchedOpt.get());

        assertNotNull(userDTO.getSystemAccountName());
        assertEquals(SystemAccountMother.DEFAULT_NAME, userDTO.getSystemAccountName());
    }

    @Test
    @Transactional
    public void shouldUpdateUserFetchedByLoginWithSystemAccount() {
        User user = UserMother.createDefaultAndPersist(em);
        em.persist(user);
        em.flush();
        UserDTO userDTO = new UserDTO(user);
        SystemAccount account = SystemAccountMother.createDifferentAndPersist(em);
        userDTO.setSystemAccountName(account.getName());
        userDTO.setSystemAccountId(account.getId());
        userService.updateUser(userDTO);

        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccountByLogin(user.getLogin());

        assertTrue(fetchedOpt.isPresent());
        assertNotNull(fetchedOpt.get().getSystemAccount());
        assertEquals(SystemAccountMother.UPDATED_NAME, fetchedOpt.get().getSystemAccount().getName());
    }

    @Test
    @Transactional
    public void shouldUpdateUserFetchedByCurrentWithSystemAccount() {
        Optional<User> fetchedOpt = userService.getUserWithAuthoritiesAndAccount();
        assertTrue(fetchedOpt.isPresent());
        UserDTO userDTO = new UserDTO(fetchedOpt.get());
        SystemAccount account = SystemAccountMother.createDifferentAndPersist(em);
        userDTO.setSystemAccountName(account.getName());
        userDTO.setSystemAccountId(account.getId());
        userService.updateUser(userDTO);

        Optional<User> resultOpt = userService.getUserWithAuthoritiesAndAccount();

        assertTrue(resultOpt.isPresent());
        assertEquals(resultOpt.get().getSystemAccount().getName(), account.getName());
        assertEquals(resultOpt.get().getSystemAccount().getId(), account.getId());
    }


    @Test
    @Transactional
    public void shouldAssignAllUsersDocsToSystemUserBeforeRemoval() {
        DocumentUpload documentUpload = DocumentUploadMother.createDefaultAndPersist(em);
        assertNotEquals(TestConstants.SYSTEM, documentUpload.getUploader().getLogin());
        int usersNumber = userRepository.findAll().size();

        userService.deleteUser(documentUpload.getUploader().getLogin());

        Optional<DocumentUpload> result = documentUploadRepository.findById(documentUpload.getId());
        assertThat(result).isPresent();
        assertEquals(documentUpload.getDateUploaded(), result.get().getDateUploaded());
        assertEquals(documentUpload.getOriginalDocumentId(), result.get().getOriginalDocumentId());
        assertEquals(documentUpload.getParsedDocumentId(), result.get().getParsedDocumentId());
        assertEquals(documentUpload.getFilename(), result.get().getFilename());
        assertEquals(TestConstants.SYSTEM, result.get().getUploader().getLogin());
        assertEquals(usersNumber - 1, userRepository.findAll().size());

        // the user cleaned up earlier
        documentUploadRepository.delete(result.get());
    }
}
