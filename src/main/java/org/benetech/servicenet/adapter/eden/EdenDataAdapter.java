package org.benetech.servicenet.adapter.eden;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.eden.model.BaseData;
import org.benetech.servicenet.adapter.eden.model.ComplexResponseElement;
import org.benetech.servicenet.adapter.eden.model.DataToPersist;
import org.benetech.servicenet.adapter.eden.model.SimpleResponseElement;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component("EdenDataAdapter")
public class EdenDataAdapter extends SingleDataAdapter {

    private static final String URL = "https://api.icarol.com/v1/Resource";
    private static final String ID = "id=";
    private static final String PARAMS_BEGINNING = "?";
    private static final String PARAMS_DELIMITER = "&";

    @Value("${scheduler.interval.eden-api-key}")
    private String edenApiKey;

    @Override
    public void importData(SingleImportData data) {
        Type collectionType = new TypeToken<Collection<SimpleResponseElement>>() { }.getType();
        Collection<SimpleResponseElement> responseElements = new Gson().fromJson(data.getSingleObjectData(), collectionType);
        gatherMoreDetails(responseElements);
    }

    private void gatherMoreDetails(Collection<SimpleResponseElement> responseElements) {
        ComplexResponseElement data = new ComplexResponseElement(responseElements);
        Map<String, String> headers = HttpUtils.getStandardHeaders(edenApiKey);
        persist(getDataToPersist(data, headers));
    }

    private void persist(DataToPersist data) {
        //TODO: persist the data
    }

    private DataToPersist getDataToPersist(ComplexResponseElement data, Map<String, String> headers) {
        DataToPersist dataToPersist = new DataToPersist();

        dataToPersist.setPrograms(collectData(data.getProgramBatches(), headers));
        dataToPersist.setSites(collectData(data.getSiteBatches(), headers));
        dataToPersist.setAgencies(collectData(data.getAgencyBatches(), headers));
        dataToPersist.setProgramAtSites(collectData(data.getProgramAtSiteBatches(), headers));

        return dataToPersist;
    }

    private <T extends BaseData> List<T> collectData(List<List<SimpleResponseElement>> batches,
                                                     Map<String, String> headers) {
        List<T> result = new ArrayList<>();
        for (List<SimpleResponseElement> batch : batches) {
            Type collectionType = new TypeToken<Collection<T>>() { }.getType();
            result.addAll(new Gson().fromJson(getData(headers, batch), collectionType));
        }
        return result;
    }

    private String getData(Map<String, String> headers, List<SimpleResponseElement> batch) {
        String params = getIdsAsQueryParameters(batch);
        String response;
        try {
            response = HttpUtils.executeGET(URL + params, headers);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with iCarol API");
        }
        return response;
    }

    private String getIdsAsQueryParameters(List<SimpleResponseElement> elements) {
        StringBuilder result = new StringBuilder(PARAMS_BEGINNING);
        for (SimpleResponseElement element : elements) {
            result.append(ID).append(element.getId()).append(PARAMS_DELIMITER);
        }
        return result.toString();
    }
}
