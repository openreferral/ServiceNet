package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@Data
public class HealthleadsOrganization extends BaseData {

    private String name;

    @SerializedName("alternate_name")
    private String alternateName;

    private String description;

    private String email;

    private String url;

    @SerializedName("tax_status")
    private String taxStatus;

    @SerializedName("tax_id")
    private String taxId;

    @SerializedName("year_incorporated")
    private String yearIncorporated;

    @SerializedName("legal_status")
    private String legalStatus;

    public LocalDate getYearIncorporated() {
        return StringUtils.isNotBlank(yearIncorporated) ? LocalDate.parse(yearIncorporated) : null;
    }
}
