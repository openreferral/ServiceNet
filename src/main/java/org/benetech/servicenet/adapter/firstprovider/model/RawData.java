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

    @SerializedName("Resource Site Email")
    private String resourceSiteEmail;

    @SerializedName("Eligibility List")
    private String eligibilityList;

    @SerializedName("Is Wheel Chair Accessible")
    private String isWheelChairAccessible;

    @SerializedName("Program List")
    private String programList;

    @SerializedName("Resource Site Phone Extension")
    private String phoneExtension;

    @SerializedName("Organization Name")
    private String organizationName;

    @SerializedName("Service Description")
    private String serviceDescription;

    private String weeklyOpenHoursRaw;

    @SerializedName("Language List")
    private String languageList;

    @SerializedName("Service Additional Info")
    private String serviceAdditionalInfo;

    private String organisationID;

    @SerializedName("City")
    private String city;

    @SerializedName("Geo Location")
    private String geoLocation;

    @SerializedName("Service Name")
    private String serviceName;

    private String resourceSiteId;

    private String isHidden;

    @SerializedName("Cost of Service Minimum")
    private String costOfServiceMinimum;

    @SerializedName("State")
    private String stateProvince;

    @SerializedName("Required Item List")
    private String requiredItems;

    @SerializedName("Cost of Service Maximum")
    private String costOfServiceMaximum;

    @SerializedName("App URL")
    private String appURL;

    private String serviceNextSteps;
}
