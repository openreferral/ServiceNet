package org.benetech.servicenet.adapter.laac;

import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.type.ListType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component("LAACDataAdapter")
public class LAACDataAdapter extends SingleDataAdapter {

    @Autowired
    private ImportService importService;

    @Override
    public DataImportReport importData(SingleImportData data) {
        List<LAACData> entities = new Gson().fromJson(data.getSingleObjectData(), new ListType<>(LAACData.class));

        return persistLAACData(entities, data.getReport(), data.getProviderName());
    }

    private DataImportReport persistLAACData(List<LAACData> data, DataImportReport report, final String providerName) {
        LAACDataMapper mapper = LAACDataMapper.INSTANCE;

        for (LAACData entity : data) {
            Organization savedOrg = importOrganization(mapper.extractOrganization(entity), entity.getId(),
                providerName, report);
            Service savedService = importService(mapper.extractService(entity),
                savedOrg, entity.getId(), providerName, report);
            Location savedLocation = importLocation(mapper.extractLocation(entity),
                savedOrg, entity.getId(), providerName);
            mapper.extractEligibility(entity).ifPresent(e -> importEligibility(e, savedService));
            mapper.extractContact(entity).ifPresent(c ->importContact(c, savedOrg));
            mapper.extractPhysicalAddress(entity).ifPresent(pa -> importPhysicalAddress(pa, savedLocation));
            importLanguages(mapper.extractLanguages(entity), savedService, savedLocation);
            importPhones(Collections.singleton(mapper.extractPhone(entity)), savedService, savedLocation);
            importServiceAtLocation(entity.getId(), providerName, savedService, savedLocation);
        }

        return report;
    }

    private Service importService(Service service, Organization savedOrg, String externalDbId,
                                  String providerName, DataImportReport report) {
        service.setOrganization(savedOrg);
        return importService.createOrUpdateService(service, externalDbId, providerName, report);
    }

    private Location importLocation(Location location, Organization savedOrg, String externalDbId, String providerName) {
        location.setOrganization(savedOrg);
        return importService.createOrUpdateLocation(location, externalDbId, providerName);
    }

    private Organization importOrganization(Organization organization, String externalDbId,
                                            String providerName, DataImportReport report) {
        return importService.createOrUpdateOrganization(organization,
            externalDbId, providerName, report);
    }

    private void importEligibility(Eligibility eligibility, Service service) {
        importService.createOrUpdateEligibility(eligibility, service);
    }

    private void importContact(Contact contact, Organization organization) {
        importService.createOrUpdateContactsForOrganization(Collections.singleton(contact), organization);
    }

    private void importLanguages(Set<Language> languages, Service service, Location location) {
        if (CollectionUtils.isNotEmpty(languages)) {
            importService.createOrUpdateLangsForService(languages, service, location);
        }
    }

    private void importPhysicalAddress(PhysicalAddress physicalAddress, Location location) {
        importService.createOrUpdatePhysicalAddress(physicalAddress, location);
    }

    private void importPhones(Set<Phone> phones, Service service, Location location) {
        importService.createOrUpdatePhonesForService(phones, service, location);
    }

    private void importServiceAtLocation(String externalDbId, String providerName, Service service, Location location) {
        importService.createOrUpdateServiceAtLocation(new ServiceAtLocation(),
            externalDbId, providerName, service, location);
    }
}
