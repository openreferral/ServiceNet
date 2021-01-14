package org.benetech.servicenet.adapter.linkforcare.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LinkForCareData {

    // ----------
    // Organization
    // ----------
    @SerializedName("ID")
    private String organizationId;

    @SerializedName("PROVIDER__C")
    private String organizationName;

    @SerializedName("CREATEDDATE")
    private String organizationCreatedDate;

    @SerializedName("LASTMODIFIEDDATE")
    private String organizationModifiedDate;

    @SerializedName("DESCRIPTION__C")
    private String organizationDescription;

    @SerializedName("WEBSITE__C")
    private String organizationWebsite;

    @SerializedName("PUBLIC_EMAIL_ADDRESS__C")
    private String organizationMainEmailAddress;

    // COVID 19 protocols
    @SerializedName("CURRENT_SERVICE_STATUS_DETAILS__C")
    private String covid19Protocols;

    @SerializedName("CURRENT_SERVICE_STATUS_FORMULA__C")
    private String covid19ProtocolsFomula;

    // Phone Number
    @SerializedName("PUBLIC_PHONE_NUMBER__C")
    private String organizationPhoneNumber;

    @SerializedName("TOLL_FREE_NUMBER__C")
    private String organizationPhoneNumberTollFree;

    // ----------
    // Location
    // ----------
    @SerializedName("ADDRESS_LINE_1__C")
    private String locationAddress1;

    @SerializedName("ADDRESS_LINE_2__C")
    private String locationAddress2;

    @SerializedName("CITY__C")
    private String locationCity;

    @SerializedName("COUNTY__C")
    private String locationCounty;

    @SerializedName("STATE__C")
    private String locationState;

    @SerializedName("ZIP_CODE__C")
    private String locationZipCode;

    // Opening Hours
    @SerializedName("FRIDAY_CLOSED_TIME__C")
    private String locationOpeningHoursFridayClosingAt;

    @SerializedName("FRIDAY_OPEN_TIME__C")
    private String locationOpeningHoursFridayOpeningAt;

    @SerializedName("MONDAY_CLOSED_TIME__C")
    private String locationOpeningHoursMondayClosingAt;

    @SerializedName("MONDAY_OPEN_TIME__C")
    private String locationOpeningHoursMondayOpeningAt;

    @SerializedName("SATURDAY_CLOSED_TIME__C")
    private String locationOpeningHoursSaturdayClosingAt;

    @SerializedName("SATURDAY_OPEN_TIME__C")
    private String locationOpeningHoursSaturdayOpeningAt;

    @SerializedName("SUNDAY_CLOSED_TIME__C")
    private String locationOpeningHoursSundayClosingAt;

    @SerializedName("SUNDAY_OPEN_TIME__C")
    private String locationOpeningHoursSundayOpeningAt;

    @SerializedName("THURSDAY_CLOSED_TIME__C")
    private String locationOpeningHoursThursdayClosingAt;

    @SerializedName("THURSDAY_OPEN_TIME__C")
    private String locationOpeningHoursThursDayOpeningAt;

    @SerializedName("TUESDAY_CLOSED_TIME__C")
    private String locationOpeningHoursTuesdayClosingAt;

    @SerializedName("TUESDAY_OPEN_TIME__C")
    private String locationOpeningHoursTuesdayOpeningAt;

    @SerializedName("WEDNESDAY_CLOSED_TIME__C")
    private String locationOpeningHoursWednesdayClosingAt;

    @SerializedName("WEDNESDAY_OPEN_TIME__C")
    private String locationOpeningHoursWednesdayOpeningAt;

    // Remote service checkbox
    @SerializedName("TELEHEALTH_HOME_MONITORING__C")
    private String locationRemoteService1;

    @SerializedName("TELE_VIDEO_VISITS_AVAILABLE__C")
    private String locationRemoteService2;

    @SerializedName("TELEPHONE_COMFORT__C")
    private String locationRemoteService3;

    @SerializedName("TELEPHONE_VISITS_AVAILABLE__C")
    private String locationRemoteService4;

    // ----------
    // Service
    // ----------
    @SerializedName("PROVIDED_SERVICE__C")
    private String serviceName;

    @SerializedName("REQUIRED_DOCUMENTATION_FORMULA__C")
    private String serviceRequiredDocuments;

    @SerializedName("OTHER_DOCUMENTATION_NEEDED__C")
    private String serviceRequiredDocumentOthers;

    @SerializedName("OTHER_APPLICATION_REGISTRATION_DETAILS__C")
    private String serviceApplicationProcess;

    @SerializedName("SERVICE_AVAILABLE_24_7__C")
    private String service247;

    // Service Type
    @SerializedName("OTHER_TYPES_OF_CARE_PROVIDED__C")
    private String serviceTypeOther;

    @SerializedName("RENAL_DIALYSIS__C")
    private String serviceTypeDialysis;

    @SerializedName("RESIDENTIAL_HEALTH_CARE__C")
    private String serviceTypeResidential;

    @SerializedName("RESPIRATORY_THERAPISTS__C")
    private String serviceTypeRespiratory;

    @SerializedName("RESPITE_CARE__C")
    private String serviceTypeRespite;

    @SerializedName("SERVICES_PROVIDED_FORMULA__C")
    private String serviceTypesProvided;

    @SerializedName("SERVICES_PROVIDED_HEALTH_CLINICS_FORMULA__C")
    private String serviceTypesProvidedHealthClinics;

    @SerializedName("SERVICES_PROVIDED_OMH_FORMULA__C")
    private String serviceTypesProvidedOmh;

    @SerializedName("SKILLED_NURSING_AND_THERAPY_SERVICES__C")
    private String serviceTypeSkilledNursing;

    @SerializedName("TRANSPORTATION__C")
    private String serviceTypeTransportation;

    @SerializedName("VETERANS__C")
    private String serviceTypeVeterans;

    @SerializedName("VOCATIONAL_WORK_PROGRAM__C")
    private String serviceTypeVocational;

    @SerializedName("WELLNESS_FACILITY__C")
    private String serviceTypeWellness;

    // Service Description
    @SerializedName("AGE_FOCUS_FORMULA__C")
    private String serviceDescriptionAge;

    @SerializedName("AGE_EXCEPTIONS_CONSIDERATIONS_COMMENTS__C")
    private String serviceDescriptionAgeExceptions;

    @SerializedName("CONDITIONS_TREATED_FORMULA__C")
    private String serviceDescriptionConditionsTreated;

    @SerializedName("COUNTIES_SERVED_FORMULA__C")
    private String serviceDescriptionContiesServed;

    @SerializedName("IN_CENTER_HEMODIALYSIS__C")
    private String serviceDescriptionInCenterHemodialysis;

    @SerializedName("IN_CENTER_NOCTURNAL_DIALYSIS__C")
    private String serviceDescriptionInCenterNocturnalDialysis;

    @SerializedName("IN_HOME_NON_MEDICAL_SERVICES__C")
    private String serviceDescriptionInHome;

    @SerializedName("IN_UNIT_WASHER_AND_DRYER__C")
    private String serviceDescriptionInUnit;

    @SerializedName("KANSAS_COUNTIES_SERVED__C")
    private String serviceDescriptionKansasCountiesServed;

    @SerializedName("LANGUAGES_SPOKEN_BY_STAFF_FORMULA__C")
    private String serviceDescriptionLanguagesSpoken;

    @SerializedName("LAUNDRY_SERVICES__C")
    private String serviceDescriptionLaundry;

    @SerializedName("LENGTH_OF_CARE_SERVICE_FORMULA__C")
    private String serviceDescriptionLengthOfService;

    @SerializedName("LICENSED_PRACTICAL_NURSES__C")
    private String serviceDescriptionLicensedNurses;

    @SerializedName("LICENSED_PROFESSIONAL_COUNSELOR__C")
    private String serviceDescriptionProfessionalCounselor;

    @SerializedName("LICENSED_STAFF_ON_DUTY_24_7__C")
    private String serviceDescriptionLicensedStaff;

    @SerializedName("LIGHT_HOUSEKEEPING__C")
    private String serviceDescriptionLightHousekeeping;

    @SerializedName("LIVE_IN_CARE__C")
    private String serviceDescriptionLiveInCare;

    @SerializedName("MEALS_SERVED_FORMULA__C")
    private String serviceDescriptionMealsServed;

    @SerializedName("MISSOURI_COUNTIES_SERVED__C")
    private String serviceDescriptionMissouriCounties;

    @SerializedName("MUST_SCHEDULE_24_HRS_OR_MORE_IN_ADVANCE__C")
    private String serviceDescriptionMustSchedule24h;

    @SerializedName("MUST_SCHEDULE_48_HRS_OR_MORE_IN_ADVANCE__C")
    private String serviceDescriptionMustSchedule48h;

    @SerializedName("OTHER_LANGUAGES_DETAILS__C")
    private String serviceDescriptionOtherLanguages;

    @SerializedName("OTHER_OPTIONS_TO_GET_SERVICES_DETAILS__C")
    private String serviceDescriptionOtherOptions;

    @SerializedName("OTHER_PROVIDED_SPECIALTY_CARE__C")
    private String serviceDescriptionOtherProvidedSpecialityCare;

    @SerializedName("OTHER_SERVICES_PROVIDED__C")
    private String serviceDescriptionOtherProvided;

    @SerializedName("PET_CARE__C")
    private String serviceDescriptionPetCare;

    @SerializedName("PET_THERAPY__C")
    private String serviceDescriptionPetTherapy;

    @SerializedName("PHARMACY_SERVICES__C")
    private String serviceDescriptionPharmacyServices;

    @SerializedName("PHYSICAL_THERAPISTS__C")
    private String serviceDescriptionPhysicalTherapists;

    @SerializedName("PHYSICIANS__C")
    private String serviceDescriptionPhysicians;

    @SerializedName("PSYCHIATRIC_NURSE__C")
    private String serviceDescriptionPsychiatricNurse;

    @SerializedName("PSYCHIATRIST__C")
    private String serviceDescriptionPsychiatrist;

    @SerializedName("PSYCHOANALYST__C")
    private String serviceDescriptionPsychoanalyst;

    @SerializedName("PSYCHOLOGIST__C")
    private String serviceDescriptionPsychologist;

    @SerializedName("PSYCHOTHERAPIST__C")
    private String serviceDescriptionPsychotherapist;

    @SerializedName("REGISTERED_DIETICIANS__C")
    private String serviceDescriptionDieticians;

    @SerializedName("REGISTERED_NURSE_ON_CALL__C")
    private String serviceDescriptionNurseOnCall;

    @SerializedName("SATOP_CLASSES__C")
    private String serviceDescriptionSatopClasses;

    @SerializedName("SERVICE_APPOINTMENT_FORMULA__C")
    private String serviceDescriptionAppointmentFormula;

    @SerializedName("SERVICE_HOURS_DETAILS__C")
    private String serviceDescriptionHoursDetails;

    @SerializedName("SERVICES_PROVIDED_INHOME_NONMED_FORMULA__C")
    private String serviceDescriptionInhomeNonmedFormula;

    @SerializedName("SINGLE_FAMILY_HOMES_VILLAS_1_BEDROOM__C")
    private String serviceDescriptionSingleFamily1Bedroom;

    @SerializedName("SINGLE_FAMILY_HOMES_VILLAS_2_BEDROOMS__C")
    private String serviceDescriptionSingleFamily2Bedrooms;

    @SerializedName("SINGLE_FAMILY_HOMES_VILLAS_3_BEDROOMS__C")
    private String serviceDescriptionSingleFamily3Bedrooms;

    @SerializedName("SKILLED_NURSING_FACILITY__C")
    private String serviceDescriptionSkilledNursingFacility;

    @SerializedName("SNACK_S__C")
    private String serviceDescriptionSnacks;

    @SerializedName("SOCIAL_WORKERS__C")
    private String serviceDescriptionSocialWorkers;

    @SerializedName("SPANISH__C")
    private String serviceDescriptionSpanish;

    @SerializedName("SPECIALTY_CARE_FORMULA__C")
    private String serviceDescriptionSpecialityCareFormula;

    @SerializedName("SPEECH_LANGUAGE_THERAPISTS__C")
    private String serviceDescriptionSpeechTherapists;

    @SerializedName("STATE_LICENSED__C")
    private String serviceDescriptionStateLicensed;

    @SerializedName("STRESS_AND_TRAUMA__C")
    private String serviceDescriptionStress;

    @SerializedName("STROKE_NEUROLOGICAL_REHABILITATION__C")
    private String serviceDescriptionStroke;

    @SerializedName("X24_7_MULTIPLE_SHIFT_CARE__C")
    private String serviceDescription247MultipleShift;

    @SerializedName("X24_HOUR_CRISIS_SERVICES__C")
    private String serviceDescription24HourCrisis;

    @SerializedName("X24_HOUR_SECURITY_PERSONNEL__C")
    private String serviceDescription24HourPersonnel;

    @SerializedName("X24_HOUR_SUPPORTIVE_SERVICES__C")
    private String serviceDescription24HourSupportive;

    @SerializedName("VISITATION_RESTRICTIONS_DETAILS__C")
    private String serviceDescriptionVisitationRestrictions;

    // Eligibility
    @SerializedName("ELIGIBILITY_FORMULA__C")
    private String serviceEligibility;

    @SerializedName("MINIMUM_AGE_ACCEPTED__C")
    private String serviceEligibilityMinAge;

    @SerializedName("MUST_BE_AMBULATORY__C")
    private String serviceEligibilityMustBeAmbulatory;

    @SerializedName("MUST_MEET_AGE_GUIDELINES__C")
    private String serviceEligibilityMustMeetAge;

    @SerializedName("MUST_MEET_DISABILITY_GUIDELINES__C")
    private String serviceEligibilityMustMeetDisability;

    @SerializedName("MUST_MEET_INCOME_GUIDELINES__C")
    private String serviceEligibilityMustMeetIncome;

    @SerializedName("MUST_BE_A_VETERAN__C")
    private String serviceEligibilityMustBeVeteran;

    @SerializedName("OTHER_ELIGIBILITY_REQUIREMENTS__C")
    private String serviceEligibilityOtherRequirements;

    @SerializedName("SOCIAL_SECURITY_CARD__C")
    private String serviceEligibilitySocialSecurityCard;
}
