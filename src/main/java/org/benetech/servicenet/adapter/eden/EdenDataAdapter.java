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
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
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

    @Override
    public DataImportReport importData(SingleImportData data) {
        Type collectionType = new TypeToken<Collection<SimpleResponseElement>>() { }.getType();
        Collection<SimpleResponseElement> responseElements = new Gson().fromJson(data.getSingleObjectData(), collectionType);
        gatherMoreDetails(responseElements, data.getReport());
        return data.getReport();
    }

    private void gatherMoreDetails(Collection<SimpleResponseElement> responseElements, DataImportReport report) {
        ComplexResponseElement data = new ComplexResponseElement(responseElements);
        Header[] headers = HttpUtils.getStandardHeaders(edenApiKey);
        persist(getDataToPersist(data, headers), report);
    }

    //TODO: do not persist some entities if they already exists
    //TODO: handle updates in reports as well
    private void persist(DataToPersist data, DataImportReport report) {
        EdenDataMapper mapper = EdenDataMapper.INSTANCE;

        persistSites(data, mapper, report);

        persistEntitiesWithoutLocation(data, mapper, report);
    }

    private void persistSites(DataToPersist data, EdenDataMapper mapper, DataImportReport report) {
        for (Site site : data.getSites()) {
            Location location = mapper.extractLocation(site.getContactDetails());
            Optional.ofNullable(location)
                .ifPresent(x -> em.persist(x));

            Optional.ofNullable(mapper.extractPhysicalAddress(site.getContactDetails()))
                .ifPresent(x -> em.persist(x.location(location)));
            Optional.ofNullable(mapper.extractPostalAddress(site.getContactDetails()))
                .ifPresent(x -> em.persist(x.location(location)));
            Optional.ofNullable(mapper.extractAccessibilityForDisabilities(site))
                .ifPresent(x -> em.persist(x.location(location)));

            List<Agency> relatedAgencies = DataCollector.findRelatedEntities(data.getAgencies(), site, AGENCY);
            persistAgencies(data.getPrograms(), mapper, location, relatedAgencies, report);
        }
    }

    private void persistEntitiesWithoutLocation(DataToPersist data, EdenDataMapper mapper, DataImportReport report) {
        persistAgencies(data.getPrograms(), mapper, null, data.getAgencies(), report);
        persistPrograms(mapper, null, null, data.getPrograms(), report);
    }

    private void persistAgencies(List<Program> programs, EdenDataMapper mapper,
                                 Location location, List<Agency> relatedAgencies, DataImportReport report) {
        for (Agency agency : relatedAgencies) {
            Organization organization = mapper.extractOrganization(agency).location(location)
                .sourceDocument(report.getDocumentUpload());
            Optional.ofNullable(organization)
                .ifPresent(x -> {
                    em.persist(x);
                    report.incrementNumberOfCreatedOrgs();
                });

            List<Program> relatedPrograms = DataCollector.findRelatedEntities(programs, agency, PROGRAM);
            persistPrograms(mapper, location, organization, relatedPrograms, report);
        }
    }

    private void persistPrograms(EdenDataMapper mapper, Location location, Organization organization,
                                 List<Program> relatedPrograms, DataImportReport report) {
        for (Program program : relatedPrograms) {
            Service service = mapper.extractService(program).organization(organization);
            Optional.ofNullable(service)
                .ifPresent(x -> {
                    em.persist(x);
                    report.incrementNumberOfCreatedServices();
                });

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
