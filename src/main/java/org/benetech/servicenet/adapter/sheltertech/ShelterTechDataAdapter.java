package org.benetech.servicenet.adapter.sheltertech;

import static org.benetech.servicenet.config.Constants.SHELTER_TECH_PROVIDER;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechOrganizationMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechPhoneMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechPhysicalAddressMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechPostalAddressMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechRegularScheduleMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechServiceMapper;
import org.benetech.servicenet.adapter.sheltertech.model.AddressRaw;
import org.benetech.servicenet.adapter.sheltertech.model.OrganizationRaw;
import org.benetech.servicenet.adapter.sheltertech.model.ServiceRaw;
import org.benetech.servicenet.adapter.sheltertech.model.ShelterTechRawData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.manager.ImportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component(SHELTER_TECH_PROVIDER + "DataAdapter")
public class ShelterTechDataAdapter extends SingleDataAdapter {

    private final Logger log = LoggerFactory.getLogger(ShelterTechDataAdapter.class);

    @Autowired
    private ImportManager importManager;

    @Override
    @Transactional
    public DataImportReport importData(SingleImportData importData) {
        ShelterTechRawData data = ShelterTechParser.collectData(importData.getSingleObjectData());
        persistOrganizations(data, importData);

        return importData.getReport();
    }

    private void persistOrganizations(ShelterTechRawData parsedData, SingleImportData rawData) {
        for (OrganizationRaw orgRaw : parsedData.getOrganizations()) {
            try {
                Organization org = ShelterTechOrganizationMapper.INSTANCE.mapToOrganization(
                    orgRaw, rawData.getReport().getDocumentUpload());

                org.setServices(getServicesToPersist(orgRaw));
                org.setLocations(getLocationsToPersist(orgRaw, org.getLocations()));

                importManager.createOrUpdateOrganization(org, org.getExternalDbId(), rawData, true);
            } catch (Exception e) {
                log.warn("Skipping organization with name: " + orgRaw.getName(), e);
            }
        }
    }

    private Set<Service> getServicesToPersist(OrganizationRaw organizationRaw) {
        Set<Service> services = new HashSet<>();
        for (ServiceRaw serviceRaw: organizationRaw.getServices()) {
            ShelterTechServiceMapper.INSTANCE.mapToService(serviceRaw).ifPresent(service -> {
                services.add(service);

                service.setRegularSchedule(getRegularScheduleToPersist(serviceRaw));
                service.setEligibility(getEligibilityToPersist(serviceRaw));
                service.setDocs(getRequiredDocumentsToPersist(serviceRaw));
                service.setTaxonomies(getServiceTaxonomiesToPersist(serviceRaw));
            });
        }
        return services;
    }

    private Set<Location> getLocationsToPersist(OrganizationRaw orgRaw, Set<Location> locations) {
        for (Location location : locations) {
            AddressRaw addressRaw = orgRaw.getAddresses().stream().filter(
                raw -> raw.getId().toString().equals(location.getExternalDbId())
            ).findFirst().orElse(null);
            if (addressRaw != null) {
                location.setPostalAddress(getPostalAddressToPersist(addressRaw));
                location.setPhysicalAddress(getPhysicalAddressToPersist(addressRaw));
            }
            location.setPhones(getPhonesToPersist(orgRaw));
        }
        return locations;
    }

    private PostalAddress getPostalAddressToPersist(AddressRaw addressRaw) {
        return ShelterTechPostalAddressMapper.INSTANCE
            .mapAddressRawToPostalAddress(addressRaw);
    }

    private PhysicalAddress getPhysicalAddressToPersist(AddressRaw addressRaw) {
        return ShelterTechPhysicalAddressMapper.INSTANCE
            .mapAddressRawToPhysicalAddress(addressRaw);
    }

    private Set<Phone> getPhonesToPersist(OrganizationRaw orgRaw) {
        return new HashSet<>(ShelterTechPhoneMapper.INSTANCE.mapToPhones(
            orgRaw.getPhones().stream().filter(phone -> phone.getNumber() != null).collect(Collectors.toList())));
    }

    private RegularSchedule getRegularScheduleToPersist(ServiceRaw serviceRaw) {
        return ShelterTechRegularScheduleMapper.INSTANCE.mapToRegularSchedule(serviceRaw.getSchedule());
    }

    private Eligibility getEligibilityToPersist(ServiceRaw serviceRaw) {
        return ShelterTechServiceMapper.INSTANCE.eligibilityFromString(serviceRaw.getEligibility());
    }

    private Set<RequiredDocument> getRequiredDocumentsToPersist(ServiceRaw serviceRaw) {
        return ShelterTechServiceMapper.INSTANCE
            .docsFromString(serviceRaw.getRequiredDocuments());
    }

    private Set<ServiceTaxonomy> getServiceTaxonomiesToPersist(ServiceRaw serviceRaw) {
        Set<ServiceTaxonomy> serviceTaxonomies = new HashSet<>();

        serviceRaw.getCategories().forEach(categoryRaw -> {
            Taxonomy taxonomy = ShelterTechServiceMapper.INSTANCE.taxonomyFromCategory(categoryRaw);
            serviceTaxonomies.add(new ServiceTaxonomy()
                .taxonomy(taxonomy)
                .providerName(SHELTER_TECH_PROVIDER)
                .externalDbId(serviceRaw.getId() + taxonomy.getExternalDbId()));
        });

        return serviceTaxonomies;
    }
}
