package org.benetech.servicenet.adapter.eden.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Accessibility {

    private String disabled;

    @SerializedName("public")
    private String description;
}
