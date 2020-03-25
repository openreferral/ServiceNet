package org.benetech.servicenet.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.listener.HibernatePostCreateListener;
import org.benetech.servicenet.listener.HibernatePostDeleteListener;
import org.benetech.servicenet.listener.HibernatePostUpdateListener;
import org.benetech.servicenet.mother.DocumentUploadMother;
import org.benetech.servicenet.mother.MetadataMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.mother.UserMother;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.dto.UserDTO;
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
public class UserProfileServiceIntTest {

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
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditingHandler auditingHandler;

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @PersistenceContext
    private EntityManager em;

    private UserProfile userProfile;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userProfile = new UserProfile();
        userProfile.setLogin("johndoe");

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }

    @Test
    @Transactional
    public void assertThatAnonymousUserIsNotGet() {
        userProfile.setLogin(Constants.ANONYMOUS_USER);
        if (!userProfileRepository.findOneByLogin(Constants.ANONYMOUS_USER).isPresent()) {
            userProfileRepository.saveAndFlush(userProfile);
        }
        final PageRequest pageable = PageRequest.of(0, (int) userProfileRepository.count());
        final Page<UserDTO> allManagedUsers = userService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent().stream()
            .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin())))
            .isTrue();
    }

    @Test
    @Transactional
    public void shouldFetchUserByIdWithAllSimpleValues() {
        UserProfile userProfile = UserMother.createDefaultAndPersist(em);
        em.persist(userProfile);
        em.flush();

        Optional<UserProfile> fetchedOpt = userService.getUser(userProfile.getId());

        assertTrue(fetchedOpt.isPresent());
        UserProfile fetched = fetchedOpt.get();
        assertEquals(userProfile.getLogin(), fetched.getLogin());
    }

    @Test
    @Transactional
    public void shouldConvertFetchedUserByIdWithSystemAccount() {
        UserProfile userProfile = UserMother.createDefaultAndPersist(em);
        em.persist(userProfile);
        em.flush();

        Optional<UserProfile> fetchedOpt = userService.getUser(userProfile.getId());
        assertTrue(fetchedOpt.isPresent());
        UserDTO userDTO = new UserDTO(fetchedOpt.get());

        assertNotNull(userDTO.getSystemAccountName());
        assertEquals(SystemAccountMother.DEFAULT_NAME, userDTO.getSystemAccountName());
    }

    @Test
    @Transactional
    public void shouldUpdateFetchedUserByIdWithSystemAccount() {
        UserProfile userProfile = UserMother.createDefaultAndPersist(em);
        em.persist(userProfile);
        em.flush();

        Optional<UserProfile> fetchedOpt = userService.getUser(userProfile.getId());
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
    public void shouldUpdateUserFetchedByCurrentWithSystemAccount() {
        Optional<UserProfile> fetchedOpt = userService.getCurrentUserOptional();
        assertTrue(fetchedOpt.isPresent());
        UserDTO userDTO = new UserDTO(fetchedOpt.get());
        SystemAccount account = SystemAccountMother.createDifferentAndPersist(em);
        userDTO.setSystemAccountName(account.getName());
        userDTO.setSystemAccountId(account.getId());
        userService.updateUser(userDTO);

        Optional<UserProfile> resultOpt = userService.getCurrentUserOptional();

        assertTrue(resultOpt.isPresent());
        assertEquals(resultOpt.get().getSystemAccount().getName(), account.getName());
        assertEquals(resultOpt.get().getSystemAccount().getId(), account.getId());
    }


    @Test
    @Transactional
    public void shouldAssignAllUsersDocsToSystemUserBeforeRemoval() {
        DocumentUpload documentUpload = DocumentUploadMother.createDefaultAndPersist(em);
        assertNotEquals(TestConstants.SYSTEM, documentUpload.getUploader().getLogin());
        int usersNumber = userProfileRepository.findAll().size();

        userProfileRepository.deleteById(documentUpload.getUploader().getId());

        Optional<DocumentUpload> result = documentUploadRepository.findById(documentUpload.getId());
        assertThat(result).isPresent();
        assertEquals(documentUpload.getDateUploaded(), result.get().getDateUploaded());
        assertEquals(documentUpload.getOriginalDocumentId(), result.get().getOriginalDocumentId());
        assertEquals(documentUpload.getParsedDocumentId(), result.get().getParsedDocumentId());
        assertEquals(documentUpload.getFilename(), result.get().getFilename());
        assertEquals(TestConstants.SYSTEM, result.get().getUploader().getLogin());
        assertEquals(usersNumber - 1, userProfileRepository.findAll().size());

        // the user cleaned up earlier
        documentUploadRepository.delete(result.get());
    }

    @Test
    @Transactional
    public void shouldAssignAllUsersMetadataToSystemUserBeforeRemoval() {
        Metadata metadata= MetadataMother.createDefaultAndPersist(em);
        assertNotEquals(TestConstants.SYSTEM, metadata.getUserProfile().getLogin());
        int usersNumber = userProfileRepository.findAll().size();

        userProfileRepository.deleteById(metadata.getUserProfile().getId());

        Optional<Metadata> result = metadataRepository.findById(metadata.getId());
        assertThat(result).isPresent();
        assertEquals(metadata.getResourceId(), result.get().getResourceId());
        assertEquals(metadata.getLastActionDate(), result.get().getLastActionDate());
        assertEquals(metadata.getLastActionType(), result.get().getLastActionType());
        assertEquals(metadata.getFieldName(), result.get().getFieldName());
        assertEquals(metadata.getPreviousValue(), result.get().getPreviousValue());
        assertEquals(metadata.getReplacementValue(), result.get().getReplacementValue());
        assertEquals(metadata.getResourceClass(), result.get().getResourceClass());
        assertEquals(TestConstants.SYSTEM, result.get().getUserProfile().getLogin());
        assertEquals(usersNumber - 1, userProfileRepository.findAll().size());

        // the user cleaned up earlier
        metadataRepository.delete(result.get());
    }
}
