package org.benetech.servicenet.adapter.laac;

import static org.benetech.servicenet.config.Constants.LAAC_PROVIDER;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.benetech.servicenet.type.ListType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("LAACDataAdapter")
public class LAACDataAdapter extends SingleDataAdapter {

    private static final String DELIMITER = ",";

    private final Logger log = LoggerFactory.getLogger(LAACDataAdapter.class);

    @Autowired
    private ImportManager importManager;

    @Autowired
    private TransactionSynchronizationService transactionSynchronizationService;

    @Override
    public DataImportReport importData(SingleImportData data) {
        List<LAACData> entities = new Gson().fromJson(data.getSingleObjectData(), new ListType<>(LAACData.class));

        return persistLAACData(entities, data);
    }

    private DataImportReport persistLAACData(List<LAACData> data, ImportData importData) {
        LAACDataMapper mapper = LAACDataMapper.INSTANCE;

        for (LAACData entity : data) {
            try {
                Location location = getLocationToPersist(mapper, entity);
                Service service = getServiceToPersist(mapper, entity);
                Organization organization = getOrganizationToPersist(mapper, entity, location, service);
                importOrganization(organization, entity.getId(), importData);
            } catch (Exception e) {
                log.warn("Skipping organization with name: " + entity.getOrganizationName(), e);
            }
        }
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations();

        return importData.getReport();
    }

    private Organization getOrganizationToPersist(LAACDataMapper mapper, LAACData entity,
                                                  Location location, Service service) {
        Organization organization = mapper.extractOrganization(entity);
        organization.setLocations(Set.of(location));
        organization.setServices(Set.of(service));
        mapper.extractContact(entity).ifPresent(c -> organization.setContacts(Set.of(c)));
        return organization;
    }

    private Location getLocationToPersist(LAACDataMapper mapper, LAACData entity) {
        Location location = mapper.extractLocation(entity);
        mapper.extractPhysicalAddress(entity).ifPresent(location::setPhysicalAddress);
        return location;
    }

    private Service getServiceToPersist(LAACDataMapper mapper, LAACData entity) {
        Service service = mapper.extractService(entity);
        mapper.extractEligibility(entity).ifPresent(service::setEligibility);
        mapper.extractPhone(entity).ifPresent(phone -> service.setPhones(Collections.singleton(phone)));
        service.setLangs(mapper.extractLanguages(entity));
        service.setTaxonomies(getServiceTaxonomiesToPersist(entity));
        return service;
    }

    private Organization importOrganization(Organization organization, String externalDbId, ImportData importData) {
        return importManager.createOrUpdateOrganization(organization, externalDbId, importData, true);
    }

    private Set<ServiceTaxonomy> getServiceTaxonomiesToPersist(LAACData entity) {
        Set<ServiceTaxonomy> serviceTaxonomies = new HashSet<>();

        if (StringUtils.isNotBlank(entity.getServiceTypes())) {
            Arrays.stream(entity.getServiceTypes().split(DELIMITER))
                .map(String::trim)
                .forEach(name -> {
                    Taxonomy taxonomy = new Taxonomy().externalDbId(name).name(name).providerName(LAAC_PROVIDER);
                    serviceTaxonomies.add(new ServiceTaxonomy()
                        .taxonomy(taxonomy)
                        .providerName(LAAC_PROVIDER)
                        .externalDbId(entity.getId() + name));
                });
        }

        return serviceTaxonomies;
    }
}
