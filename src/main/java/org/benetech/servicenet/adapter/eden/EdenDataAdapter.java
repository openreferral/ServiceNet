package org.benetech.servicenet.adapter.eden;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.eden.model.EdenAgency;
import org.benetech.servicenet.adapter.eden.model.EdenComplexResponseElement;
import org.benetech.servicenet.adapter.eden.model.EdenDataToPersist;
import org.benetech.servicenet.adapter.eden.model.EdenProgram;
import org.benetech.servicenet.adapter.eden.model.EdenProgramAtSite;
import org.benetech.servicenet.adapter.eden.model.EdenSimpleResponseElement;
import org.benetech.servicenet.adapter.eden.model.EdenSite;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@Component("EdenDataAdapter")
public class EdenDataAdapter extends SingleDataAdapter {

    private static final String AGENCY = "Agency";
    private static final String PROGRAM = "Program";
    private static final String SITE = "Site";
    private static final String PROGRAM_AT_SITE = "ProgramAtSite";
    private static final String TYPE = "type";

    @Value("${scheduler.interval.eden-api-key}")
    private String edenApiKey;

    @Autowired
    private EntityManager em;

    @Autowired
    private ImportService importService;

    @Override
    public DataImportReport importData(SingleImportData importData) {
        EdenDataToPersist dataToPersist = gatherMoreDetails(importData);
        return persist(dataToPersist, importData);
    }

    private EdenDataToPersist gatherMoreDetails(SingleImportData importData) {
        Header[] headers = HttpUtils.getStandardHeaders(edenApiKey);
        return getDataToPersist(importData.getSingleObjectData(), headers, importData.isFileUpload());
    }

    private DataImportReport persist(EdenDataToPersist data, ImportData importData) {
        EdenDataMapper mapper = EdenDataMapper.INSTANCE;
        persistSites(data, mapper, importData);
        persistEntitiesWithoutLocation(data, mapper, importData);
        return importData.getReport();
    }

    private void persistSites(EdenDataToPersist dataToPersist, EdenDataMapper mapper, ImportData importData) {
        for (EdenSite site : dataToPersist.getSites()) {
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

                List<EdenAgency> relatedAgencies = EdenDataCollector.findRelatedEntities(dataToPersist.getAgencies(),
                    site, AGENCY);
                persistAgencies(dataToPersist.getPrograms(), mapper, savedLocation, relatedAgencies, importData);
            });
        }
    }

    private void persistEntitiesWithoutLocation(EdenDataToPersist data, EdenDataMapper mapper, ImportData importData) {
        persistAgencies(data.getPrograms(), mapper, null, data.getAgencies(), importData);
        persistPrograms(mapper, null, null, data.getPrograms(), importData);
    }

    private void persistAgencies(List<EdenProgram> programs, EdenDataMapper mapper,
                                 Location location, List<EdenAgency> relatedAgencies, ImportData importData) {
        for (EdenAgency agency : relatedAgencies) {
            Organization extractedOrganization = mapper
                .extractOrganization(agency, agency.getId(), importData.getProviderName())
                .sourceDocument(importData.getReport().getDocumentUpload());

            Organization savedOrganization = importService
                .createOrUpdateOrganization(extractedOrganization, agency.getId(), importData.getProviderName(),
                    importData.getReport());

            List<EdenProgram> relatedPrograms = EdenDataCollector.findRelatedEntities(programs, agency, PROGRAM);
            persistPrograms(mapper, location, savedOrganization, relatedPrograms, importData);
        }
    }

    private void persistPrograms(EdenDataMapper mapper, Location location, Organization organization,
                                 List<EdenProgram> relatedPrograms, ImportData importData) {
        for (EdenProgram program : relatedPrograms) {
            Service extractedService = mapper.extractService(program, program.getId(), importData.getProviderName())
                .organization(organization);

            Service savedService = importService
                .createOrUpdateService(extractedService, program.getId(), importData.getProviderName(),
                    importData.getReport());

            mapper.extractEligibility(program).ifPresent(
                x -> importService.createOrUpdateEligibility(x, savedService));

            importService.createOrUpdatePhones(mapper.extractPhones(program.getContactDetails()), savedService, location);
            importService.createOrUpdateLangs(mapper.extractLangs(program), savedService, location);
            importService.createOrUpdateOpeningHours(mapper.extractOpeningHours(program.getHours()), savedService, location);
        }
    }

    private EdenDataToPersist getDataToPersist(String dataString, Header[] headers, boolean isFileUpload) {
        if (isFileUpload) {
            return collectDataDetailsFromTheFile(dataString);
        }
        Type collectionType = new TypeToken<Collection<EdenSimpleResponseElement>>() {
        }.getType();
        Collection<EdenSimpleResponseElement> responseElements = new Gson().fromJson(dataString, collectionType);
        EdenComplexResponseElement data = new EdenComplexResponseElement(responseElements);
        return collectDataDetailsFromTheApi(data, headers);
    }

    private EdenDataToPersist collectDataDetailsFromTheFile(String file) {
        EdenDataToPersist dataToPersist = new EdenDataToPersist();
        JsonArray elements = new Gson().fromJson(file, JsonArray.class);

        for (JsonElement element : elements) {
            JsonObject jsonObject = element.getAsJsonObject();
            String type = jsonObject.get(TYPE).getAsString();
            updateData(dataToPersist, jsonObject, type);
        }

        return dataToPersist;
    }

    private void updateData(EdenDataToPersist dataToPersist, JsonObject jsonObject, String type) {
        if (type.equals(AGENCY)) {
            dataToPersist.addAgency(new Gson().fromJson(jsonObject, EdenAgency.class));
        }
        if (type.equals(PROGRAM)) {
            dataToPersist.addProgram(new Gson().fromJson(jsonObject, EdenProgram.class));
        }
        if (type.equals(PROGRAM_AT_SITE)) {
            dataToPersist.addProgramAtSite(new Gson().fromJson(jsonObject, EdenProgramAtSite.class));
        }
        if (type.equals(SITE)) {
            dataToPersist.addSite(new Gson().fromJson(jsonObject, EdenSite.class));
        }
    }

    private EdenDataToPersist collectDataDetailsFromTheApi(EdenComplexResponseElement data, Header[] headers) {
        EdenDataToPersist dataToPersist = new EdenDataToPersist();

        dataToPersist.setPrograms(EdenDataCollector.collectData(data.getProgramBatches(), headers,
            EdenProgram.class));
        dataToPersist.setSites(EdenDataCollector.collectData(data.getSiteBatches(), headers, EdenSite.class));
        dataToPersist.setAgencies(EdenDataCollector.collectData(data.getAgencyBatches(), headers, EdenAgency.class));
        dataToPersist.setProgramAtSites(EdenDataCollector.collectData(data.getProgramAtSiteBatches(),
            headers, EdenProgramAtSite.class));

        return dataToPersist;
    }
}
