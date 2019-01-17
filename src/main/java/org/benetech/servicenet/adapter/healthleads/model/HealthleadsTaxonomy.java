package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class HealthleadsTaxonomy extends HealthleadsBaseData {

    private String name;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("parent_name")
    private String parentName;

    private String vocabulary;
}
