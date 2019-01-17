package org.benetech.servicenet.adapter.smcconnect;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.smcconnect.model.SmcBaseData;
import org.benetech.servicenet.type.ListType;

import java.util.ArrayList;
import java.util.List;

class DataResolver {

    public List<SmcBaseData> getDataFromJson(final String json, final String filename) {
        DataType type;
        try {
            type = DataType.valueOf(filename.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ArrayList<>();
        }
        return getDataFromJson(json, type.getClazz());
    }

    private List<SmcBaseData> getDataFromJson(final String json, Class<? extends SmcBaseData> clazz) {
        return new Gson().fromJson(json, new ListType<>(clazz));
    }
}
