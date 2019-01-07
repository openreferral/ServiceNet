package org.benetech.servicenet.adapter.eden;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.eden.model.Agency;
import org.benetech.servicenet.adapter.eden.model.ComplexResponseElement;
import org.benetech.servicenet.adapter.eden.model.DataToPersist;
import org.benetech.servicenet.adapter.eden.model.Program;
import org.benetech.servicenet.adapter.eden.model.ProgramAtSite;
import org.benetech.servicenet.adapter.eden.model.SimpleResponseElement;
import org.benetech.servicenet.adapter.eden.model.Site;
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

    @Value("${scheduler.interval.eden-api-key}")
    private String edenApiKey;

    @Autowired
    private EntityManager em;

    @Autowired
    private ImportService importService;

    @Override
    public DataImportReport importData(SingleImportData data) {
        Type collectionType = new TypeToken<Collection<SimpleResponseElement>>() {
        }.getType();
        Collection<SimpleResponseElement> responseElements = new Gson().fromJson(data.getSingleObjectData(), collectionType);
        gatherMoreDetails(responseElements, data);
        return data.getReport();
    }

    private void gatherMoreDetails(Collection<SimpleResponseElement> responseElements, ImportData importData) {
        ComplexResponseElement data = new ComplexResponseElement(responseElements);
        Header[] headers = HttpUtils.getStandardHeaders(edenApiKey);
        persist(getDataToPersist(data, headers), importData);
    }

    private void persist(DataToPersist data, ImportData importData) {
        EdenDataMapper mapper = EdenDataMapper.INSTANCE;

        persistSites(data, mapper, importData);

        persistEntitiesWithoutLocation(data, mapper, importData);
    }

    private void persistSites(DataToPersist dataToPersist, EdenDataMapper mapper, ImportData importData) {
        for (Site site : dataToPersist.getSites()) {
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

                List<Agency> relatedAgencies = DataCollector.findRelatedEntities(dataToPersist.getAgencies(), site, AGENCY);
                persistAgencies(dataToPersist.getPrograms(), mapper, savedLocation, relatedAgencies, importData);
            });
        }
    }

    private void persistEntitiesWithoutLocation(DataToPersist data, EdenDataMapper mapper, ImportData importData) {
        persistAgencies(data.getPrograms(), mapper, null, data.getAgencies(), importData);
        persistPrograms(mapper, null, null, data.getPrograms(), importData);
    }

    private void persistAgencies(List<Program> programs, EdenDataMapper mapper,
                                 Location location, List<Agency> relatedAgencies, ImportData importData) {
        for (Agency agency : relatedAgencies) {
            Organization extractedOrganization = mapper
                .extractOrganization(agency, agency.getId(), importData.getProviderName())
                .sourceDocument(importData.getReport().getDocumentUpload());

            Organization savedOrganization = importService
                .createOrUpdateOrganization(extractedOrganization, agency.getId(), importData.getProviderName(),
                    importData.getReport());

            List<Program> relatedPrograms = DataCollector.findRelatedEntities(programs, agency, PROGRAM);
            persistPrograms(mapper, location, savedOrganization, relatedPrograms, importData);
        }
    }

    private void persistPrograms(EdenDataMapper mapper, Location location, Organization organization,
                                 List<Program> relatedPrograms, ImportData importData) {
        for (Program program : relatedPrograms) {
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

    private DataToPersist getDataToPersist(ComplexResponseElement data, Header[] headers) {
        DataToPersist dataToPersist = new DataToPersist();

        dataToPersist.setPrograms(DataCollector.collectData(data.getProgramBatches(), headers,
            Program.class));
        dataToPersist.setSites(DataCollector.collectData(data.getSiteBatches(), headers, Site.class));
        dataToPersist.setAgencies(DataCollector.collectData(data.getAgencyBatches(), headers, Agency.class));
        dataToPersist.setProgramAtSites(DataCollector.collectData(data.getProgramAtSiteBatches(),
            headers, ProgramAtSite.class));

        return dataToPersist;
    }
}
