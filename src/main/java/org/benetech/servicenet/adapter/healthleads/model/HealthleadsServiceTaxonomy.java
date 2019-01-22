package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthleadsServiceTaxonomy extends ServiceRelatedHealthleadsData {

    @SerializedName("taxonomy_id")
    private String taxonomyId;

    @SerializedName("taxonomy_detail")
    private String taxonomyDetail;
}
