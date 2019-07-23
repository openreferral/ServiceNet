package org.benetech.servicenet.adapter.anonymous;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.anonymous.model.RawData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * DataAdapter for the first example data set
 */
@Component("AnonymousDataAdapter")
public class AnonymousDataAdapter extends SingleDataAdapter {

    @Autowired
    private EntityManager em;

    @Autowired
    private LocationService locationService;

    @Override
    public DataImportReport importData(SingleImportData data) {
        Type listType = new TypeToken<ArrayList<RawData>>() {
        }.getType();
        List<RawData> entries = new Gson().fromJson(data.getSingleObjectData(), listType);

        AnonymousDataMapper mapper = AnonymousDataMapper.INSTANCE;

        //TODO: do not persist some entities if they already exists
        //TODO: handle updates in reports as well
        for (RawData rawData : entries) {
            Location location = mapper.extractLocation(rawData);
            locationService.save(location);

            Organization organization = mapper.extractOrganization(rawData)
                .locations(Collections.singleton(location))
                .active(true).sourceDocument(data.getReport().getDocumentUpload());
            em.persist(organization);
            data.getReport().incrementNumberOfCreatedOrgs();

            Service service = mapper.extractService(rawData).organization(organization);
            em.persist(service);
            data.getReport().incrementNumberOfCreatedServices();

            em.persist(mapper.extractPhysicalAddress(rawData).location(location));
            em.persist(mapper.extractPostalAddress(rawData).location(location));
            em.persist(mapper.extractPhone(rawData).location(location).srvc(service));
            em.persist(mapper.extractEligibility(rawData).srvc(service));
            em.persist(mapper.extractAccessibilityForDisabilities(rawData)
                .location(location));

            mapper.extractPrograms(rawData)
                .stream().map(p -> p.organization(organization))
                .forEach(p -> em.persist(p));
            mapper.extractLangs(rawData)
                .stream().map(loc -> loc.srvc(service).location(location))
                .forEach(p -> em.persist(p));
            List<OpeningHours> openingHours = mapper.extractOpeningHours(rawData);
            openingHours.forEach(o -> em.persist(o));
            em.persist(new RegularSchedule().openingHours(new HashSet<>(openingHours)).location(location).srvc(service));
        }
        return data.getReport();
    }
}
