package org.benetech.servicenet.adapter.healthleads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.ZonedDateTime;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsBaseData;
import org.benetech.servicenet.type.ListType;

import java.util.ArrayList;
import java.util.List;
import org.benetech.servicenet.util.ZonedDateTimeDeserializer;

class DataResolver {
    public static final String HEALTHLEADS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    List<HealthleadsBaseData> getDataFromJson(final String json, final String filename) {
        DataType type;
        try {
            type = DataType.valueOf(filename.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ArrayList<>();
        }
        return getDataFromJson(json, type.getClazz());
    }

    private List<HealthleadsBaseData> getDataFromJson(final String json, Class<? extends HealthleadsBaseData> clazz) {
        return getHealthleadsGson().fromJson(json, new ListType<>(clazz));
    }

    private static Gson getHealthleadsGson() {
        return new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer(HEALTHLEADS_DATE_FORMAT))
            .create();
    }
}
