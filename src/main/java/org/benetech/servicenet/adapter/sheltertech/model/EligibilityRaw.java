package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EligibilityRaw {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

}
