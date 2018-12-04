package org.benetech.servicenet.adapter.firstprovider.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RawData {

    private String resourceVerifiedDate;

    private String resourceCreatedDate;

    @SerializedName("Resource Site Phone")
    private String phone;

    private String programCategoryList;

    private String serviceID;

    private String line2;

    @SerializedName("Line 1")
    private String address1;

    private String isCapacitySensitive;

    private String resourceSiteEmail;

    private String eligibilityList;

    private String isWheelChairAccessible;

    private String programList;

    private String resourceSitePhoneExtension;

    private String organizationName;

    private String serviceDescription;

    private String weeklyOpenHoursRaw;

    private String languageList;

    private String serviceAdditionalInfo;

    private String organisationID;

    @SerializedName("City")
    private String city;

    @SerializedName("Geo Location")
    private String geoLocation;

    private String serviceName;

    private String resourceSiteId;

    private String isHidden;

    private String costOfServiceMinimum;

    @SerializedName("State")
    private String stateProvince;

    private String requiredItemList;

    private String costOfServiceMaximum;

    private String appURL;

    private String serviceNextSteps;
}
