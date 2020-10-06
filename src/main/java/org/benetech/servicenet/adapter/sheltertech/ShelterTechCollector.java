package org.benetech.servicenet.adapter.sheltertech;

import org.benetech.servicenet.util.HttpUtils;

import java.io.IOException;

public final class ShelterTechCollector {

    private static final String URL = "http://askdarcel.org/api/resources?category_id=all";

    public static String getData() {
        String response;
        try {
            response = HttpUtils.executeGET(URL, HttpUtils.getStandardHeaders());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with ShelterTech API", e);
        }
        return response;
    }

    private ShelterTechCollector() {
    }

}
