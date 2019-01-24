package org.benetech.servicenet.adapter.sheltertech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.benetech.servicenet.adapter.sheltertech.model.ShelterTechRawData;
import org.benetech.servicenet.adapter.sheltertech.type.adapter.LocalDateTimeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public final class ShelterTechParser {

    private static final Logger LOG = LoggerFactory.getLogger(ShelterTechParser.class);

    private static final Gson GSON = getShelterTechGson();

    public static ShelterTechRawData collectData(String file) {
        LOG.debug("Parsing ShelterTech json.");
        return GSON.fromJson(file, ShelterTechRawData.class);
    }

    private static Gson getShelterTechGson() {
        TypeAdapter<LocalDateTime> dateTypeAdapter = new LocalDateTimeAdapter();
        TypeAdapter<LocalDateTime> safeDateTypeAdapter = dateTypeAdapter.nullSafe();

        return new GsonBuilder()
            .setDateFormat(LocalDateTimeAdapter.DATE_FORMAT)
            .registerTypeAdapter(LocalDateTime.class, safeDateTypeAdapter)
            .create();
    }

    private ShelterTechParser() {
    }

}
