package org.benetech.servicenet.service.impl;

import java.time.ZonedDateTime;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationError;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.FundingRepository;
import org.benetech.servicenet.service.OrganizationImportService;
import org.benetech.servicenet.service.SharedImportService;
import org.benetech.servicenet.service.SystemAccountService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.benetech.servicenet.service.util.EntityManagerUtils.updateCollection;

@Slf4j
@Component
public class OrganizationImportServiceImpl implements OrganizationImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private FundingRepository fundingRepository;

    @Autowired
    private SharedImportService sharedImportService;

    @Override
    @Transactional
    public Organization createOrUpdateOrganization(Organization filledOrganization, Organization organizationFromDb,
        String externalDbId, String providerName, DataImportReport report, boolean overwriteLastUpdated) {
        Organization organization = new Organization(filledOrganization);
        Optional<SystemAccount> systemAccount = systemAccountService.findByName(providerName);

        if (systemAccount.isEmpty()) {
            return null;
        }
        organization.setAccount(systemAccount.get());

        EntityValidator.validateAndFix(organization, filledOrganization, report, externalDbId);

        if (organizationFromDb != null) {
            if (organizationFromDb.deepEquals(filledOrganization)) {
                log.info("Organization " + organization.getName() + " didn't change, skipping");
                return null;
            }
            fillDataFromDb(organization, organizationFromDb);
            organization.setNeedsMatching(true);
            em.merge(organization);
            report.incrementNumberOfUpdatedOrgs();
        } else {
            organization.setNeedsMatching(true);
            em.persist(organization);
            report.incrementNumberOfCreatedOrgs();
        }

        createOrUpdateFundingForOrganization(filledOrganization.getFunding(), organization);
        createOrUpdateProgramsForOrganization(filledOrganization.getPrograms(), organization);
        createOrUpdateContactsForOrganization(filledOrganization.getContacts(), organization);
        createOrUpdatePhonesForOrganization(filledOrganization.getPhones(), organization);

        if (filledOrganization.getUpdatedAt() != null && !overwriteLastUpdated) {
            organization.setUpdatedAt(filledOrganization.getUpdatedAt());
        } else {
            organization.setUpdatedAt(ZonedDateTime.now());
        }
        // Set null to indicate, that update was done through import instead by user
        organization.setUpdatedBy(null);
        Organization org = em.merge(organization);

        Set<OrganizationError> errors = new HashSet<>();
        for (OrganizationError organizationError : report.getOrganizationErrors()) {
            organizationError.setOrganization(org);
            errors.add(organizationError);
        }
        report.setOrganizationErrors(errors);
        return org;
    }

    @ConfidentialFilter
    private void createOrUpdateFundingForOrganization(Funding funding, Organization organization) {
        if (funding != null) {
            EntityValidator.validateAndFix(funding, organization, null, "");
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
        contacts.forEach(p -> {
            EntityValidator.validateAndFix(p, organization, null, "");
            p.setOrganization(organization);
        });
        organization.setContacts(sharedImportService.createOrUpdateContacts(contacts));
    }

    private void createOrUpdatePhonesForOrganization(Set<Phone> phones, Organization organization) {
        phones.forEach(p -> {
            EntityValidator.validateAndFix(p, organization, null, "");
            p.setOrganization(organization);
        });
        sharedImportService.persistPhones(organization.getPhones(), phones);
    }

    private void createOrUpdateFilteredProgramsForOrganization(Set<Program> programs, Organization organization) {
        programs.forEach(p -> {
            EntityValidator.validateAndFix(p, organization, null, "");
            p.setOrganization(organization);
        });
        updateCollection(em, organization.getPrograms(), programs, (p1, p2) ->
            p1.getName().equals(p2.getName()) && StringUtils.equals(p1.getAlternateName(), p2.getAlternateName()));
    }

    private void fillDataFromDb(Organization newOrg, Organization orgFromDb) {
        newOrg.setId(orgFromDb.getId());
        newOrg.setAccount(orgFromDb.getAccount());
        newOrg.setFunding(orgFromDb.getFunding());
        newOrg.setContacts(orgFromDb.getContacts());
        newOrg.setPrograms(orgFromDb.getPrograms());
        newOrg.setReplacedBy(orgFromDb.getReplacedBy());
        newOrg.setAdditionalSilos(orgFromDb.getAdditionalSilos());
        newOrg.setPhones(orgFromDb.getPhones());
        if (newOrg.getUserProfiles() == null || newOrg.getUserProfiles().isEmpty()
            && orgFromDb.getUserProfiles() != null && !orgFromDb.getUserProfiles().isEmpty()) {
            newOrg.setUserProfiles(orgFromDb.getUserProfiles());
        }
    }
}
