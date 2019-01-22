package org.benetech.servicenet.adapter.icarol;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.icarol.model.ICarolBaseData;
import org.benetech.servicenet.adapter.icarol.model.ICarolRelated;
import org.benetech.servicenet.adapter.icarol.model.ICarolSimpleResponseElement;
import org.benetech.servicenet.util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class ICarolDataCollector {

    private static final String URL = "https://api.icarol.com/v1/Resource";
    private static final String ID = "id=";
    private static final String PARAMS_BEGINNING = "?";
    private static final String PARAMS_DELIMITER = "&";

    static JsonArray getData(Header[] headers, List<ICarolSimpleResponseElement> batch) {
        String params = getIdsAsQueryParameters(batch);
        String response;
        try {
            response = HttpUtils.executeGET(URL + params, headers);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with iCarol API");
        }
        return new Gson().fromJson(response, JsonArray.class);
    }

    static <T extends ICarolBaseData> List<T> collectData(List<List<ICarolSimpleResponseElement>> batches,
                                                          Header[] headers, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        JsonArray jsonArray = new JsonArray();
        for (List<ICarolSimpleResponseElement> batch : batches) {
            jsonArray.addAll(getData(headers, batch));
        }

        Gson gson = new Gson();
        for (JsonElement object : jsonArray) {
            result.add(gson.fromJson(object, clazz));
        }

        return result;
    }

    static <T extends ICarolBaseData, V extends ICarolBaseData> List<T> findRelatedEntities(
        List<T> entities, V relatedEntity, String type) {
        List<String> relatedIds = findRelatedIds(relatedEntity, type);
        List<T> result = new ArrayList<>();
        Iterator<T> iterator = entities.iterator();

        while (iterator.hasNext()) {
            T entity = iterator.next();
            if (relatedIds.contains(entity.getId())) {
                result.add(entity);
                iterator.remove();
            }
        }

        return result;
    }

    private static <T extends ICarolBaseData> List<String> findRelatedIds(T entity, String type) {
        List<String> agenciesIds = new ArrayList<>();
        for (ICarolRelated related : entity.getRelated()) {
            if (related.getType().equals(type)) {
                agenciesIds.add(related.getId());
            }
        }
        return agenciesIds;
    }

    private static String getIdsAsQueryParameters(List<ICarolSimpleResponseElement> elements) {
        StringBuilder result = new StringBuilder(PARAMS_BEGINNING);
        for (ICarolSimpleResponseElement element : elements) {
            result.append(ID).append(element.getId()).append(PARAMS_DELIMITER);
        }
        return result.toString();
    }

    private ICarolDataCollector() {
    }
}
