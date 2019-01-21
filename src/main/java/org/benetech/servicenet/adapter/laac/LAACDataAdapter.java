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
            Service service = saveService(mapper.extractService(entity), entity.getId(), providerName, report);
            Location location = saveLocation(mapper.extractLocation(entity), entity.getId(), providerName);
            Organization organization = saveOrganization(mapper.extractOrganization(entity), entity.getId(),
                providerName, service, location, report);

            saveEligibility(mapper.extractEligibility(entity), service);
            saveContact(mapper.extractContact(entity), organization);
            saveLanguages(mapper.extractLanguages(entity), service, location);
            savePhysicalAddress(mapper.extractPhysicalAddress(entity), location);
            savePhones(Collections.singleton(mapper.extractPhone(entity)), service, location);
            saveServiceAtLocation(entity.getId(), providerName, service, location);
        }

        return report;
    }

    private Service saveService(Service service, String externalDbId, String providerName, DataImportReport report) {
        return importService.createOrUpdateService(service, externalDbId, providerName, report);
    }

    private Location saveLocation(Location location, String externalDbId, String providerName) {
        return importService.createOrUpdateLocation(location, externalDbId, providerName);
    }

    private Organization saveOrganization(Organization organization, String externalDbId, String providerName,
                                          Service service, Location location, DataImportReport report) {
        return importService.createOrUpdateOrganization(organization,
            externalDbId, providerName, service, location, report);
    }

    private void saveEligibility(Eligibility eligibility, Service service) {
        if (eligibility != null && service != null) {
            importService.createOrUpdateEligibility(eligibility, service);
        }
    }

    private void saveContact(Contact contact, Organization organization) {
        if (contact != null) {
            importService.createOrUpdateContactsForOrganization(Collections.singleton(contact), organization);
        }
    }

    private void saveLanguages(Set<Language> languages, Service service, Location location) {
        if (CollectionUtils.isNotEmpty(languages)) {
            importService.createOrUpdateLangsForService(languages, service, location);
        }
    }

    private void savePhysicalAddress(PhysicalAddress physicalAddress, Location location) {
        if (physicalAddress != null) {
            importService.createOrUpdatePhysicalAddress(physicalAddress, location);
        }
    }

    private void savePhones(Set<Phone> phones, Service service, Location location) {
        importService.createOrUpdatePhonesForService(phones, service, location);
    }

    private void saveServiceAtLocation(String externalDbId, String providerName, Service service, Location location) {
        importService.createOrUpdateServiceAtLocation(new ServiceAtLocation(),
            externalDbId, providerName, service, location);
    }
}
