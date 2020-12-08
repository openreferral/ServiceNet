package org.benetech.servicenet.service.impl;

import static org.benetech.servicenet.service.impl.ActivityFilterServiceImpl.SILO_TAXONOMIES;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.ZeroCodeSpringJUnit5Extension;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.mother.SiloMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.mother.UserMother;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@WithMockUser(username = "admin")
@ExtendWith({ SpringExtension.class, ZeroCodeSpringJUnit5Extension.class })
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ActivityFilterServiceImplTest {

    @Autowired
    private ActivityFilterService activityFilterService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    private static SystemAccount systemAccount;
    private static SystemAccount anotherSystemAccount;
    private static Silo silo;
    private static Silo anotherSilo;
    private static UserProfile currentUser;
    private static UserProfile anotherUser;

    @BeforeAll
    public static void beforeAll(@Autowired TestDatabaseManagement testDatabaseManagement) {
        testDatabaseManagement.clearDb();
    }

    @BeforeEach
    public void beforeEach() {
        systemAccount = SystemAccountMother.createDefaultAndPersist(em);
        anotherSystemAccount = SystemAccountMother.createDifferentAndPersist(em);
        silo = SiloMother.createDefaultAndPersist(em);
        anotherSilo = SiloMother.createDifferentAndPersist(em);
        currentUser = userService.getCurrentUserProfile();
        currentUser.setSystemAccount(systemAccount);
        em.persist(currentUser);
        anotherUser = UserMother.createDifferentForServiceProviderAndPersist(em, anotherSystemAccount);
        em.flush();
    }

    @Test
    @Transactional
    public void getTaxonomiesForAllProviders() {
        List<Taxonomy> taxonomies = Arrays.asList(
            createAssignedTaxonomy("taxonomy", systemAccount, null, null),
            createAssignedTaxonomy("anotherTaxonomy", systemAccount, null, null)
        );
        List<Taxonomy> taxonomiesWithAnotherAccount = Arrays.asList(
            createAssignedTaxonomy("taxonomyASA", anotherSystemAccount, null, null),
            createAssignedTaxonomy("anotherTaxonomyASA", anotherSystemAccount, null, null)
        );

        Map<String, Set<String>> taxonomyMap = activityFilterService.getTaxonomies(null, null, null);
        assertEquals(2, taxonomyMap.size());

        Set<String> taxonomyNames = taxonomyMap.get(systemAccount.getName());
        assertThat(
            taxonomyNames,
            containsInAnyOrder(taxonomies.stream().map(Taxonomy::getName).toArray())
        );
        assertEquals(taxonomies.size(), taxonomyNames.size());
        Set<String> taxonomyNamesFromAnotherAccount = taxonomyMap.get(anotherSystemAccount.getName());
        assertThat(
            taxonomyNamesFromAnotherAccount,
            containsInAnyOrder(taxonomiesWithAnotherAccount.stream().map(Taxonomy::getName).toArray())
        );
        assertEquals(taxonomiesWithAnotherAccount.size(), taxonomyNamesFromAnotherAccount.size());
    }

    @Test
    @Transactional
    public void getTaxonomiesForASpecificProvider() {
        List<Taxonomy> taxonomies = Arrays.asList(
            createAssignedTaxonomy("taxonomy", systemAccount, null, null),
            createAssignedTaxonomy("anotherTaxonomy", systemAccount, null, null)
        );
        List<Taxonomy> taxonomiesWithAnotherAccount = Collections.singletonList(
            createAssignedTaxonomy("taxonomyASA", anotherSystemAccount, null, null)
        );

        Map<String, Set<String>> taxonomyMap = activityFilterService.getTaxonomies(null, systemAccount.getName(), null);
        assertEquals(1, taxonomyMap.size());

        Set<String> taxonomyNames = taxonomyMap.get(systemAccount.getName());
        assertThat(
            taxonomyNames,
            containsInAnyOrder(taxonomies.stream().map(Taxonomy::getName).toArray())
        );
        assertEquals(taxonomies.size(), taxonomyNames.size());
    }

    @Test
    @Transactional
    public void getAllTaxonomiesWithAdditionalSilo() {
        List<Taxonomy> taxonomies = Arrays.asList(
            createAssignedTaxonomy("taxonomy", systemAccount, null, silo),
            createAssignedTaxonomy("anotherTaxonomy", systemAccount, null, null)
        );
        List<Taxonomy> taxonomiesWithAnotherAccount = Arrays.asList(
            createAssignedTaxonomy("taxonomyASA", anotherSystemAccount, null, silo),
            createAssignedTaxonomy("anotherTaxonomyASA", anotherSystemAccount, null, anotherSilo)
        );

        Map<String, Set<String>> taxonomyMap = activityFilterService.getTaxonomies(silo.getId(), null, null);
        assertEquals(3, taxonomyMap.size());

        Set<String> taxonomyNames = taxonomyMap.get(systemAccount.getName());
        assertThat(
            taxonomyNames,
            containsInAnyOrder(taxonomies.stream().map(Taxonomy::getName).toArray())
        );
        assertEquals(2, taxonomyNames.size());

        Set<String> taxonomyNamesFromAnotherAccount = taxonomyMap.get(anotherSystemAccount.getName());
        assertThat(
            taxonomyNamesFromAnotherAccount,
            containsInAnyOrder(taxonomiesWithAnotherAccount.stream().map(Taxonomy::getName).toArray())
        );
        assertEquals(taxonomiesWithAnotherAccount.size(), taxonomyNamesFromAnotherAccount.size());

        Set<String> siloTaxonomyNames = taxonomyMap.get(SILO_TAXONOMIES);
        assertThat(
            siloTaxonomyNames,
            containsInAnyOrder(taxonomies.get(0).getName(), taxonomiesWithAnotherAccount.get(0).getName())
        );
        assertEquals(2, siloTaxonomyNames.size());
    }

    @Test
    @Transactional
    public void getTaxonomiesForASpecificProviderWithAdditionalSilo() {
        List<Taxonomy> taxonomies = Arrays.asList(
            createAssignedTaxonomy("taxonomy", systemAccount, null, silo),
            createAssignedTaxonomy("anotherTaxonomy", systemAccount, null, anotherSilo)
        );
        List<Taxonomy> taxonomiesWithAnotherAccount = Arrays.asList(
            createAssignedTaxonomy("taxonomyASA", anotherSystemAccount, null, silo),
            createAssignedTaxonomy("anotherTaxonomyASA", anotherSystemAccount, null, null)
        );

        Map<String, Set<String>> taxonomyMap = activityFilterService.getTaxonomies(silo.getId(), systemAccount.getName(), null);
        assertEquals(2, taxonomyMap.size());

        Set<String> taxonomyNames = taxonomyMap.get(systemAccount.getName());
        // should get all the taxonomies that either have a system account or silo match
        assertThat(
            taxonomyNames,
            containsInAnyOrder(taxonomies.get(0).getName(), taxonomiesWithAnotherAccount.get(0).getName())
        );
        assertEquals(2, taxonomyNames.size());
        // for consistency, the taxonomies are also returned under "silo" entry
        Set<String> siloTaxonomyNames = taxonomyMap.get(SILO_TAXONOMIES);
        assertThat(
            siloTaxonomyNames,
            containsInAnyOrder(taxonomies.get(0).getName(), taxonomiesWithAnotherAccount.get(0).getName())
        );
        assertEquals(2, siloTaxonomyNames.size());
    }

    @Test
    @Transactional
    public void getTaxonomiesWithUserExclusion() {
        List<Taxonomy> taxonomies = Arrays.asList(
            createAssignedTaxonomy("taxonomy", systemAccount, currentUser, null),
            createAssignedTaxonomy("anotherTaxonomy", systemAccount, anotherUser, null)
        );
        List<Taxonomy> taxonomiesWithAnotherAccount = Arrays.asList(
            createAssignedTaxonomy("taxonomyASA", anotherSystemAccount, currentUser, null),
            createAssignedTaxonomy("anotherTaxonomyASA", anotherSystemAccount, anotherUser, null)
        );

        Map<String, Set<String>> taxonomyMap = activityFilterService.getTaxonomies(null, systemAccount.getName(), anotherUser);
        assertEquals(1, taxonomyMap.size());

        Set<String> taxonomyNames = taxonomyMap.get(systemAccount.getName());
        assertThat(
            taxonomyNames,
            containsInAnyOrder(taxonomies.get(0).getName())
        );
        assertEquals(1, taxonomyNames.size());
    }

    private Taxonomy createAssignedTaxonomy(String taxonomyName,
        SystemAccount systemAccount, UserProfile userProfile, Silo additionalSilo) {
        Service service = new Service();
        Taxonomy taxonomy = new Taxonomy()
            .name(taxonomyName)
            .providerName(systemAccount.getName());
        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy().taxonomy(taxonomy).srvc(service);
        service.setTaxonomies(Collections.singleton(serviceTaxonomy));
        Organization organization = new Organization()
            .active(true)
            .services(Collections.singleton(service))
            .account(systemAccount)
            .additionalSilos(Collections.singleton(additionalSilo))
            .userProfiles(Collections.singleton(userProfile));
        service.setOrganization(organization);
        if (additionalSilo != null) {
            Set<Organization> organizations = additionalSilo.getOrganizations();
            if (organizations == null) {
                organizations = new HashSet<>();
            }
            organizations.add(organization);
            additionalSilo.setOrganizations(organizations);
        }
        em.persist(taxonomy);
        em.persist(serviceTaxonomy);
        em.persist(service);
        em.persist(organization);
        if (additionalSilo != null) {
            em.persist(additionalSilo);
        }
        em.flush();
        return taxonomy;
    }
}
