package org.benetech.servicenet.adapter.sheltertech;

import org.benetech.servicenet.adapter.sheltertech.model.ShelterTechRawData;
import org.benetech.servicenet.util.HttpUtils;

import java.io.IOException;

final class ShelterTechCollector {

    private static final String URL = "http://askdarcel.org/api/resources?category_id=all";

    static ShelterTechRawData getData() {
        String response;
        try {
            response = HttpUtils.executeGET(URL, HttpUtils.getStandardHeaders());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with ShelterTech API");
        }
        return ShelterTechParser.collectData(response);
    }

    private ShelterTechCollector() {
    }

}
