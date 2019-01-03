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
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

        persistEntitiesWithoutLocation(data, documentUpload, mapper);
    }

    private void persistSites(DataToPersist data, DocumentUpload documentUpload, EdenDataMapper mapper,
                              String providerName) {
        for (Site site : data.getSites()) {
            mapper.extractLocation(site.getContactDetails(), site.getId(), providerName)
                .ifPresent(extractedLocation -> {
                Location savedLocation = importService.createOrUpdate(extractedLocation, site.getId(), providerName);

                mapper.extractPhysicalAddress(site.getContactDetails()).ifPresent(
                    x -> importService.createOrUpdate(x, savedLocation));
                mapper.extractPostalAddress(site.getContactDetails()).ifPresent(
                    x -> importService.createOrUpdate(x, savedLocation));
                mapper.extractAccessibilityForDisabilities(site).ifPresent(
                    x -> importService.createOrUpdate(x, savedLocation));

                List<Agency> relatedAgencies = DataCollector.findRelatedEntities(data.getAgencies(), site, AGENCY);
                persistAgencies(data.getPrograms(), documentUpload, mapper, savedLocation, relatedAgencies);
            });
        }
    }

    private void persistEntitiesWithoutLocation(DataToPersist data, DocumentUpload documentUpload, EdenDataMapper mapper) {
        persistAgencies(data.getPrograms(), documentUpload, mapper, null, data.getAgencies());
        persistPrograms(mapper, null, null, data.getPrograms());
    }

    private void persistAgencies(List<Program> programs, DocumentUpload documentUpload, EdenDataMapper mapper,
                                 Location location, List<Agency> relatedAgencies) {
        for (Agency agency : relatedAgencies) {
            Organization organization = mapper.extractOrganization(agency).location(location)
                .sourceDocument(documentUpload);
            Optional.ofNullable(organization)
                .ifPresent(x -> em.persist(x));

            List<Program> relatedPrograms = DataCollector.findRelatedEntities(programs, agency, PROGRAM);
            persistPrograms(mapper, location, organization, relatedPrograms);
        }
    }

    private void persistPrograms(EdenDataMapper mapper, Location location, Organization organization,
                                 List<Program> relatedPrograms) {
        for (Program program : relatedPrograms) {
            Service service = mapper.extractService(program).organization(organization);
            Optional.ofNullable(service)
                .ifPresent(x -> em.persist(x));

            Optional.ofNullable(mapper.extractPhone(program.getContactDetails()))
                .ifPresent(x -> em.persist(x.location(location).srvc(service)));
            Optional.ofNullable(mapper.extractEligibility(program))
                .ifPresent(x -> em.persist(x.srvc(service)));
            mapper.extractLangs(program)
                .stream().map(language -> language.srvc(service).location(location))
                .forEach(p -> em.persist(p));

            List<OpeningHours> openingHours = mapper.extractOpeningHours(program.getHours());
            openingHours.forEach(o -> em.persist(o));
            em.persist(new RegularSchedule().openingHours(new HashSet<>(openingHours)).location(location).srvc(service));
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
