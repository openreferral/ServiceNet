package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SmcProgram extends SmcBaseData {

    @SerializedName("organization_id")
    private String organizationId;

    @SerializedName("alternate_name")
    private String alternateName;

    private String name;
}
