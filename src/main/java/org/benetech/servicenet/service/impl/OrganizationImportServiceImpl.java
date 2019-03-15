package org.benetech.servicenet.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.FundingRepository;
import org.benetech.servicenet.service.OrganizationImportService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.SystemAccountService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.benetech.servicenet.service.util.EntityManagerUtils.safeRemove;

@Component
public class OrganizationImportServiceImpl implements OrganizationImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private FundingRepository fundingRepository;

    @Override
    @Transactional
    public Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId,
                                                   String providerName, DataImportReport report) {
        Organization organization = new Organization(filledOrganization);
        Optional<SystemAccount> systemAccount = systemAccountService.findByName(providerName);

        if (!systemAccount.isPresent()) {
            return null;
        }
        organization.setAccount(systemAccount.get());
        if (EntityValidator.isNotValid(organization, report, externalDbId)) {
            return null;
        }

        Optional<Organization> organizationFromDb =
            organizationService.findWithEagerAssociations(externalDbId, providerName);
        if (organizationFromDb.isPresent()) {
            fillDataFromDb(organization, organizationFromDb.get());
            em.merge(organization);
            report.incrementNumberOfUpdatedOrgs();
        } else {
            em.persist(organization);
            report.incrementNumberOfCreatedOrgs();
        }

        createOrUpdateFundingForOrganization(filledOrganization.getFunding(), organization);
        createOrUpdateProgramsForOrganization(filledOrganization.getPrograms(), organization);
        createOrUpdateContactsForOrganization(filledOrganization.getContacts(), organization);

        return organization;
    }

    @ConfidentialFilter
    private void createOrUpdateFundingForOrganization(Funding funding, Organization organization) {
        if (funding != null) {
            Optional<Funding> fundingFormDb = fundingRepository.findOneByOrganizationId(organization.getId());
            funding.setOrganization(organization);
            if (fundingFormDb.isPresent()) {
                funding.setId(fundingFormDb.get().getId());
                em.merge(funding);
            } else {
                em.persist(funding);
            }
            organization.setFunding(funding);
        }
    }

    private void createOrUpdateProgramsForOrganization(Set<Program> programs, Organization organization) {
        Set<Program> filtered = programs.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredProgramsForOrganization(filtered, organization);
    }

    private void createOrUpdateContactsForOrganization(Set<Contact> contacts, Organization organization) {
        Set<Contact> filtered = contacts.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredContactsForOrganization(filtered, organization);
    }

    private void createOrUpdateFilteredContactsForOrganization(Set<Contact> contacts, Organization organization) {
        contacts.forEach(p -> p.setOrganization(organization));

        Set<Contact> common = new HashSet<>(contacts);
        common.retainAll(organization.getContacts());

        organization.getContacts().stream().filter(c -> !common.contains(c)).forEach(c -> safeRemove(em, c));
        contacts.stream().filter(c -> !common.contains(c)).forEach(c -> em.persist(c));

        organization.setContacts(contacts);
    }

    private void createOrUpdateFilteredProgramsForOrganization(Set<Program> programs, Organization organization) {
        programs.forEach(p -> p.setOrganization(organization));

        Set<Program> common = new HashSet<>(programs);
        common.retainAll(organization.getPrograms());

        organization.getPrograms().stream().filter(p -> !common.contains(p)).forEach(p -> safeRemove(em, p));
        programs.stream().filter(p -> !common.contains(p)).forEach(p -> em.persist(p));

        organization.setPrograms(programs);
    }

    private void fillDataFromDb(Organization newOrg, Organization orgFromDb) {
        newOrg.setId(orgFromDb.getId());
        newOrg.setAccount(orgFromDb.getAccount());
        newOrg.setFunding(orgFromDb.getFunding());
        newOrg.setContacts(orgFromDb.getContacts());
        newOrg.setPrograms(orgFromDb.getPrograms());
    }
}
