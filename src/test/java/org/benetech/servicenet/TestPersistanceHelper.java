package org.benetech.servicenet;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.repository.LocationRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.repository.ServiceRepository;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.HolidayScheduleService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.ProgramService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
public class TestPersistanceHelper {

    @Autowired
    private EntityManager em;

    // region services
    @Autowired
    private LocationService locationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private AccessibilityForDisabilitiesService accessibilityService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private TaxonomyService taxonomyService;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private HolidayScheduleService holidayScheduleService;

    @Autowired
    private TestPersistanceHelper helper;

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private TaxonomyRepository taxonomyRepository;

    //endregion

    @Transactional
    public <T> void persist(T obj) {
        em.persist(obj);
    }

    @Transactional
    public void flush() {
        em.flush();
    }

    @Transactional
    public SystemAccount generateSystemAccount(String provider) {
        Optional<SystemAccount> accountFromDb = systemAccountRepository.findByName(provider);
        if (accountFromDb.isPresent()) {
            return accountFromDb.get();
        }
        SystemAccount account = new SystemAccount().name(provider);
        em.persist(account);
        em.flush();
        em.refresh(account);
        return account;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Location generateExistingLocation(String id, String provider, String string) {
        Optional<Location> locationFormDb = locationRepository.findOneByExternalDbIdAndProviderName(id, provider);
        if (locationFormDb.isPresent()) {
            return locationFormDb.get();
        }
        Location result = new Location().externalDbId(id).providerName(provider)
            .name(string);
        em.persist(result);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Service generateExistingService(String id, String provider, String string) {
        Optional<Service> serviceFromDb = serviceRepository.findOneByExternalDbIdAndProviderName(id, provider);
        if (serviceFromDb.isPresent()) {
            return serviceFromDb.get();
        }
        Service result = new Service().externalDbId(id).providerName(provider)
            .name(string);
        em.persist(result);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Organization generateExistingOrganization(String id, String string,
                                                      SystemAccount account, boolean bool) {
        Optional<Organization> organizationFromDb = organizationRepository.findOneByExternalDbIdAndProviderName(id, account.getName());
        if (organizationFromDb.isPresent()) {
            return organizationFromDb.get();
        }
        Organization result = new Organization().externalDbId(id).account(account)
            .name(string).active(bool);
        em.persist(result);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Taxonomy generateExistingTaxonomy(String id, String provider, String string) {
        Optional<Taxonomy> taxonomyFromDb = taxonomyRepository.findOneByExternalDbIdAndProviderName(id, provider);
        if (taxonomyFromDb.isPresent()) {
            return taxonomyFromDb.get();
        }
        Taxonomy result = new Taxonomy().externalDbId(id).providerName(provider)
            .name(string);
        em.persist(result);
        return result;
    }
}
