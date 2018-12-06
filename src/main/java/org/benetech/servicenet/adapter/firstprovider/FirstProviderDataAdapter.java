package org.benetech.servicenet.adapter.firstprovider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.NotImplementedException;
import org.benetech.servicenet.adapter.AbstractDataAdapter;
import org.benetech.servicenet.adapter.firstprovider.model.RawData;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * DataAdapter for the first example data set
 */
@Component("FirstProviderDataAdapter")
public class FirstProviderDataAdapter extends AbstractDataAdapter {

    @Autowired
    private EntityManager em;

    @Override
    public void persistData(String data, DocumentUpload documentUpload) {
        Type listType = new TypeToken<ArrayList<RawData>>() { }.getType();
        List<RawData> entries = new Gson().fromJson(data, listType);

        FirstProviderDataMapper mapper = FirstProviderDataMapper.INSTANCE;

        //TODO: do note persist some entities if they already exists
        for (RawData rawData : entries) {
            Location location = mapper.extractLocation(rawData);
            em.persist(location);

            Organization organization = mapper.extractOrganization(rawData)
                .location(location).active(true).sourceDocument(documentUpload);
            em.persist(organization);

            Service service = mapper.extractService(rawData).organization(organization);
            em.persist(service);

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
        }
    }

    @Override
    public void persistData(List<String> data, DocumentUpload documentUpload) {
        throw new NotImplementedException(MULTIPLE_MAPPING_NOT_SUPPORTED);
    }

    @Override
    public boolean isUsedForSingleObjects() {
        return true;
    }
}
