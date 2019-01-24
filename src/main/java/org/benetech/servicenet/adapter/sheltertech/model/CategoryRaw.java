package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CategoryRaw {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private Integer id;

    @SerializedName("top_level")
    private Boolean topLevel;

    @SerializedName("featured")
    private Boolean featured;

}
