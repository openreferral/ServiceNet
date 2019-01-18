package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RawData {

    @SerializedName("resources")
    private List<OrganizationRaw> organizations;

}
