package org.benetech.servicenet.adapter.laac;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.type.ListType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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
            Service service = importService.createOrUpdateService(
                mapper.extractService(entity),
                entity.getId(), providerName, report);

            Location location = importService.createOrUpdateLocation(
                mapper.extractLocation(entity),
                entity.getId(), providerName);

            Organization organization = importService.createOrUpdateOrganization(
                mapper.extractOrganization(entity),
                entity.getId(), providerName, service, location, report);

            importService.createOrUpdateEligibility(
                mapper.extractEligibility(entity),
                service);

            importService.createOrUpdateContactsForOrganization(
                Collections.singleton(mapper.extractContact(entity)), organization);

            importService.createOrUpdateLangsForService(
                mapper.extractLanguages(entity),
                service, location);

            importService.createOrUpdatePhysicalAddress(
                mapper.extractPhysicalAddress(entity),
                location);

            importService.createOrUpdatePhonesForService(
                Collections.singleton(mapper.extractPhone(entity)),
                    service, location);

            importService.createOrUpdateServiceAtLocation(new ServiceAtLocation(),
                entity.getId(), providerName, service, location);
        }

        return report;
    }
}
