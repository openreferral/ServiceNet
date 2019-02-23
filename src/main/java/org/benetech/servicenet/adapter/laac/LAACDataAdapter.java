package org.benetech.servicenet.adapter.laac;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.type.ListType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component("LAACDataAdapter")
public class LAACDataAdapter extends SingleDataAdapter {

    @Autowired
    private ImportManager importManager;

    @Override
    public DataImportReport importData(SingleImportData data) {
        List<LAACData> entities = new Gson().fromJson(data.getSingleObjectData(), new ListType<>(LAACData.class));

        return persistLAACData(entities, data);
    }

    private DataImportReport persistLAACData(List<LAACData> data, ImportData importData) {
        LAACDataMapper mapper = LAACDataMapper.INSTANCE;

        for (LAACData entity : data) {
            Location location = getLocationToPersist(mapper, entity);
            Service service = getServiceToPersist(mapper, entity);
            Organization organization = getOrganizationToPersist(mapper, entity, location, service);
            importOrganization(organization, entity.getId(), importData);
        }

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
        service.setPhones(Collections.singleton(mapper.extractPhone(entity)));
        service.setLangs(mapper.extractLanguages(entity));
        return service;
    }

    private Organization importOrganization(Organization organization, String externalDbId, ImportData importData) {
        return importManager.createOrUpdateOrganization(organization, externalDbId, importData);
    }
}
