package org.benetech.servicenet.adapter.icarol;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolComplexResponseElement;
import org.benetech.servicenet.adapter.icarol.model.ICarolDataToPersist;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgramAtSite;
import org.benetech.servicenet.adapter.icarol.model.ICarolSimpleResponseElement;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public abstract class AbstractICarolDataAdapter extends SingleDataAdapter {

    private static final String AGENCY = "Agency";
    private static final String PROGRAM = "Program";
    private static final String SITE = "Site";
    private static final String SERVICE_SITE = "ServiceSite";
    private static final String TYPE = "type";

    @Autowired
    private EntityManager em;

    @Autowired
    private ImportService importService;

    @Override
    public DataImportReport importData(SingleImportData importData) {
        ICarolDataToPersist dataToPersist = gatherMoreDetails(importData);
        return persist(dataToPersist, importData);
    }

    protected abstract String getApiKey();

    private ICarolDataToPersist gatherMoreDetails(SingleImportData importData) {
        Header[] headers = HttpUtils.getStandardHeaders(getApiKey());
        return getDataToPersist(importData.getSingleObjectData(), headers, importData.isFileUpload());
    }

    private DataImportReport persist(ICarolDataToPersist data, ImportData importData) {
        ICarolDataMapper mapper = ICarolDataMapper.INSTANCE;
        persistSites(data, mapper, importData);
        persistEntitiesWithoutLocation(data, mapper, importData);
        return importData.getReport();
    }

    private void persistSites(ICarolDataToPersist dataToPersist, ICarolDataMapper mapper, ImportData importData) {
        for (ICarolSite site : dataToPersist.getSites()) {
            mapper.extractLocation(site.getContactDetails(), site.getId(), importData.getProviderName())
                .ifPresent(extractedLocation -> {
                Location savedLocation = importService.createOrUpdateLocation(extractedLocation, site.getId(),
                    importData.getProviderName());

                mapper.extractPhysicalAddress(site.getContactDetails()).ifPresent(
                    x -> importService.createOrUpdatePhysicalAddress(x, savedLocation));
                mapper.extractPostalAddress(site.getContactDetails()).ifPresent(
                    x -> importService.createOrUpdatePostalAddress(x, savedLocation));
                mapper.extractAccessibilityForDisabilities(site).ifPresent(
                    x -> importService.createOrUpdateAccessibility(x, savedLocation));

                List<ICarolAgency> relatedAgencies = ICarolDataCollector.findRelatedEntities(dataToPersist.getAgencies(),
                    site, AGENCY);
                persistAgencies(dataToPersist.getPrograms(), mapper, savedLocation, relatedAgencies, importData);
            });
        }
    }

    private void persistEntitiesWithoutLocation(ICarolDataToPersist data, ICarolDataMapper mapper, ImportData importData) {
        persistAgencies(data.getPrograms(), mapper, null, data.getAgencies(), importData);
        persistPrograms(mapper, null, null, data.getPrograms(), importData);
    }

    private void persistAgencies(List<ICarolProgram> programs, ICarolDataMapper mapper,
                                 Location location, List<ICarolAgency> relatedAgencies, ImportData importData) {
        for (ICarolAgency agency : relatedAgencies) {
            Organization extractedOrganization = mapper
                .extractOrganization(agency, agency.getId(), importData.getProviderName())
                .sourceDocument(importData.getReport().getDocumentUpload());

            Organization savedOrganization = importService
                .createOrUpdateOrganization(extractedOrganization, agency.getId(), importData.getProviderName(),
                    importData.getReport());

            List<ICarolProgram> relatedPrograms = ICarolDataCollector.findRelatedEntities(programs, agency, PROGRAM);
            persistPrograms(mapper, location, savedOrganization, relatedPrograms, importData);
        }
    }

    private void persistPrograms(ICarolDataMapper mapper, Location location, Organization organization,
                                 List<ICarolProgram> relatedPrograms, ImportData importData) {
        for (ICarolProgram program : relatedPrograms) {
            Service extractedService = mapper.extractService(program, program.getId(), importData.getProviderName())
                .organization(organization);

            Service savedService = importService
                .createOrUpdateService(extractedService, program.getId(), importData.getProviderName(),
                    importData.getReport());

            mapper.extractEligibility(program).ifPresent(
                x -> importService.createOrUpdateEligibility(x, savedService));

            importService.createOrUpdatePhones(mapper.extractPhones(program.getContactDetails()), savedService, location);
            importService.createOrUpdateLangs(mapper.extractLangs(program), savedService, location);
            importService.createOrUpdateOpeningHours(mapper.extractOpeningHours(program.getHours()), savedService);
        }
    }

    private ICarolDataToPersist getDataToPersist(String dataString, Header[] headers, boolean isFileUpload) {
        if (isFileUpload) {
            return collectDataDetailsFromTheFile(dataString);
        }
        Type collectionType = new TypeToken<Collection<ICarolSimpleResponseElement>>() {
        }.getType();
        Collection<ICarolSimpleResponseElement> responseElements = new Gson().fromJson(dataString, collectionType);
        ICarolComplexResponseElement data = new ICarolComplexResponseElement(responseElements);
        return collectDataDetailsFromTheApi(data, headers);
    }

    private ICarolDataToPersist collectDataDetailsFromTheFile(String file) {
        ICarolDataToPersist dataToPersist = new ICarolDataToPersist();
        JsonArray elements = new Gson().fromJson(file, JsonArray.class);

        for (JsonElement element : elements) {
            JsonObject jsonObject = element.getAsJsonObject();
            String type = jsonObject.get(TYPE).getAsString();
            updateData(dataToPersist, jsonObject, type);
        }

        return dataToPersist;
    }

    private void updateData(ICarolDataToPersist dataToPersist, JsonObject jsonObject, String type) {
        if (type.equals(AGENCY)) {
            dataToPersist.addAgency(new Gson().fromJson(jsonObject, ICarolAgency.class));
        }
        if (type.equals(PROGRAM)) {
            dataToPersist.addProgram(new Gson().fromJson(jsonObject, ICarolProgram.class));
        }
        if (type.equals(SERVICE_SITE)) {
            dataToPersist.addServiceSite(new Gson().fromJson(jsonObject, ICarolProgramAtSite.class));
        }
        if (type.equals(SITE)) {
            dataToPersist.addSite(new Gson().fromJson(jsonObject, ICarolSite.class));
        }
    }

    private ICarolDataToPersist collectDataDetailsFromTheApi(ICarolComplexResponseElement data, Header[] headers) {
        ICarolDataToPersist dataToPersist = new ICarolDataToPersist();

        dataToPersist.setPrograms(ICarolDataCollector.collectData(data.getProgramBatches(), headers,
            ICarolProgram.class));
        dataToPersist.setSites(ICarolDataCollector.collectData(data.getSiteBatches(), headers, ICarolSite.class));
        dataToPersist.setAgencies(ICarolDataCollector.collectData(data.getAgencyBatches(), headers, ICarolAgency.class));
        dataToPersist.setProgramAtSites(ICarolDataCollector.collectData(data.getProgramAtSiteBatches(),
            headers, ICarolProgramAtSite.class));

        return dataToPersist;
    }
}
