package org.benetech.servicenet.adapter.sheltertech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.benetech.servicenet.adapter.sheltertech.model.OrganizationRaw;
import org.benetech.servicenet.adapter.sheltertech.model.RawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

final class Parser {

    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    private static final Gson GSON = getShelterTechGson();

    static void collectData(String file) {
        RawData main = GSON.fromJson(file, RawData.class);
        List<OrganizationRaw> organizations = main.getOrganizations();

        for (OrganizationRaw organization : organizations) {
            LOG.debug(organization.toString());
        }
    }

    private static Gson getShelterTechGson() {
        TypeAdapter<LocalDateTime> dateTypeAdapter = new LocalDateTimeAdapter();
        TypeAdapter<LocalDateTime> safeDateTypeAdapter = dateTypeAdapter.nullSafe();

        return new GsonBuilder()
            .setDateFormat(LocalDateTimeAdapter.DATE_FORMAT)
            .registerTypeAdapter(LocalDateTime.class, safeDateTypeAdapter)
            .create();
    }

    private Parser() {
    }

}
