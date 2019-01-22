package org.benetech.servicenet.adapter.icarol.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ICarolAccessibility {

    private String disabled;

    @SerializedName("public")
    private String description;
}
