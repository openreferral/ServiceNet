package org.benetech.servicenet.adapter.firstprovider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.NotImplementedException;
import org.benetech.servicenet.adapter.AbstractDataAdapter;
import org.benetech.servicenet.adapter.firstprovider.model.RawData;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
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
    public void persistData(String data) {
        Type listType = new TypeToken<ArrayList<RawData>>() { }.getType();
        List<RawData> entries = new Gson().fromJson(data, listType);

        FirstProviderDataMapper mapper = FirstProviderDataMapper.INSTANCE;

        for (RawData rawData : entries) {
            PhysicalAddress physicalAddress = mapper.extractPhysicalAddress(rawData);
            em.persist(physicalAddress);

            PostalAddress postalAddress = mapper.extractPostalAddress(rawData);
            em.persist(postalAddress);

            Location location = mapper.extractLocation(rawData, physicalAddress, postalAddress);
            em.persist(location);

            Phone phone = mapper.extractPhone(rawData, location);
            em.persist(phone);

            //TODO: map other entities as well
        }
    }

    @Override
    public void persistData(List<String> data) {
        throw new NotImplementedException(MULTIPLE_MAPPING_NOT_SUPPORTED);
    }

    @Override
    public boolean isUsedForSingleObjects() {
        return true;
    }
}
