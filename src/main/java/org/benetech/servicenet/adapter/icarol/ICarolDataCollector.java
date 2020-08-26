package org.benetech.servicenet.adapter.icarol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.icarol.model.ICarolBaseData;
import org.benetech.servicenet.adapter.icarol.model.ICarolRelated;
import org.benetech.servicenet.adapter.icarol.model.ICarolSimpleResponseElement;
import org.benetech.servicenet.util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.benetech.servicenet.util.ZonedDateTimeDeserializer;

@Slf4j
final class ICarolDataCollector {

    private static final String ID = "id=";
    private static final String PARAMS_BEGINNING = "?";
    private static final String PARAMS_DELIMITER = "&";
    private static final int PART_1 = 1;
    private static final int PART_2 = 2;
    private static final int PART_3 = 3;
    private static final int PART_4 = 4;

    static JsonArray getData(Header[] headers, List<ICarolSimpleResponseElement> batch, String uri) {
        String params = getIdsAsQueryParameters(batch);
        String response;
        try {
            response = HttpUtils.executeGET(uri + params, headers);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with iCarol API", e);
        }
        return getICarolGson().fromJson(response, JsonArray.class);
    }

    static <T extends ICarolBaseData> List<T> collectData(List<List<ICarolSimpleResponseElement>> batches,
                                                          Header[] headers, Class<T> clazz, String uri) {
        List<T> result = new ArrayList<>();
        JsonArray jsonArray = new JsonArray();
        int i = 0;
        for (List<ICarolSimpleResponseElement> batch : batches) {
            logProgress(clazz, i++, batches.size());
            jsonArray.addAll(getData(headers, batch, uri));
        }

        Gson gson = getICarolGson();
        for (JsonElement object : jsonArray) {
            result.add(gson.fromJson(object, clazz));
        }

        return result;
    }

    private static void logProgress(Class clazz, int batchNr, int size) {
        int part = getProcessPart(clazz);
        int maxPercentage = 100;
        log.info("Collecting data for ICarol (part " + part + " of 4): " + maxPercentage * batchNr / size + "% completed");
    }

    private static int getProcessPart(Class clazz) {
        switch (clazz.getSimpleName()) {
            case "ICarolProgram":
                return PART_1;
            case "ICarolSite":
                return PART_2;
            case "ICarolAgency":
                return PART_3;
            case "ICarolServiceSite":
                return PART_4;
            default:
                return 0;
        }
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

    private static Gson getICarolGson() {
        return new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer())
            .create();
    }
}
