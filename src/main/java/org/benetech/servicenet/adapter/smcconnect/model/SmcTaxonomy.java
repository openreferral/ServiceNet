package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SmcTaxonomy {

    private String id;

    private String depth;

    @SerializedName("taxonomy_id")
    private String taxonomyId;

    private String name;

    @SerializedName("parent_id")
    private String parentId;
}
