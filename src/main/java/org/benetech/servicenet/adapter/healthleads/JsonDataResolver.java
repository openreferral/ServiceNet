package org.benetech.servicenet.adapter.healthleads;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.healthleads.model.BaseData;
import org.benetech.servicenet.type.ListType;

import java.util.ArrayList;
import java.util.List;

public class JsonDataResolver {

    public List<BaseData> getDataFromJson(final String json, final String filename) {
        DataType type;
        try {
            type = DataType.valueOf(filename.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ArrayList<>();
        }
        return getDataFromJson(json, type.getClazz());
    }

    private List<BaseData> getDataFromJson(final String json, Class<? extends BaseData> clazz) {
        return new Gson().fromJson(json, new ListType<>(clazz));
    }
}
