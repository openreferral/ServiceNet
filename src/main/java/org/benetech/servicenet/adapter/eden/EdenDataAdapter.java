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
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DocumentUpload;
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
    public void importData(SingleImportData data) {
        Type collectionType = new TypeToken<Collection<SimpleResponseElement>>() { }.getType();
        Collection<SimpleResponseElement> responseElements = new Gson().fromJson(data.getSingleObjectData(), collectionType);
        gatherMoreDetails(responseElements, data.getDocumentUpload(), data.getProviderName());
    }

    private void gatherMoreDetails(Collection<SimpleResponseElement> responseElements, DocumentUpload documentUpload,
                                   String providerName) {
        ComplexResponseElement data = new ComplexResponseElement(responseElements);
        Header[] headers = HttpUtils.getStandardHeaders(edenApiKey);
        persist(getDataToPersist(data, headers), documentUpload, providerName);
    }

    //TODO: do not persist some entities if they already exists
    private void persist(DataToPersist data, DocumentUpload documentUpload, String providerName) {
        EdenDataMapper mapper = EdenDataMapper.INSTANCE;

        persistSites(data, documentUpload, mapper, providerName);

        persistEntitiesWithoutLocation(data, documentUpload, mapper, providerName);
    }

    private void persistSites(DataToPersist data, DocumentUpload documentUpload, EdenDataMapper mapper,
                              String providerName) {
        for (Site site : data.getSites()) {
            mapper.extractLocation(site.getContactDetails(), site.getId(), providerName)
                .ifPresent(extractedLocation -> {
                Location savedLocation = importService.createOrUpdateLocation(extractedLocation, site.getId(), providerName);

                mapper.extractPhysicalAddress(site.getContactDetails()).ifPresent(
                    x -> importService.createOrUpdatePhysicalAddress(x, savedLocation));
                mapper.extractPostalAddress(site.getContactDetails()).ifPresent(
                    x -> importService.createOrUpdatePostalAddress(x, savedLocation));
                mapper.extractAccessibilityForDisabilities(site).ifPresent(
                    x -> importService.createOrUpdateAccessibility(x, savedLocation));

                List<Agency> relatedAgencies = DataCollector.findRelatedEntities(data.getAgencies(), site, AGENCY);
                persistAgencies(data.getPrograms(), documentUpload, mapper, savedLocation, relatedAgencies, providerName);
            });
        }
    }

    private void persistEntitiesWithoutLocation(DataToPersist data, DocumentUpload documentUpload, EdenDataMapper mapper,
                                                String providerName) {
        persistAgencies(data.getPrograms(), documentUpload, mapper, null, data.getAgencies(), providerName);
        persistPrograms(mapper, null, null, data.getPrograms(), providerName);
    }

    private void persistAgencies(List<Program> programs, DocumentUpload documentUpload, EdenDataMapper mapper,
                                 Location location, List<Agency> relatedAgencies, String providerName) {
        for (Agency agency : relatedAgencies) {
            Organization extractedOrganization = mapper.extractOrganization(agency, agency.getId(), providerName)
                .sourceDocument(documentUpload);

            Organization savedOrganization = importService
                .createOrUpdateOrganization(extractedOrganization, agency.getId(), providerName);

            List<Program> relatedPrograms = DataCollector.findRelatedEntities(programs, agency, PROGRAM);
            persistPrograms(mapper, location, savedOrganization, relatedPrograms, providerName);
        }
    }

    private void persistPrograms(EdenDataMapper mapper, Location location, Organization organization,
                                 List<Program> relatedPrograms, String providerName) {
        for (Program program : relatedPrograms) {
            Service extractedService = mapper.extractService(program, program.getId(), providerName)
                .organization(organization);

            Service savedService = importService
                .createOrUpdateService(extractedService, program.getId(), providerName);

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
