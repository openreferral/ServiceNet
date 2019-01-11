package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;

import java.util.Set;

public interface ImportService {

    Location createOrUpdateLocation(Location location, String externalDbId, String providerName);

    PhysicalAddress createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress, Location location);

    PostalAddress createOrUpdatePostalAddress(PostalAddress postalAddress, Location location);

    AccessibilityForDisabilities createOrUpdateAccessibility(AccessibilityForDisabilities accessibility, Location location);

    Organization createOrUpdateOrganization(Organization organization, String externalDbId, String providerName,
                                            DataImportReport report);

    Service createOrUpdateService(Service service, String externalDbId, String providerName, DataImportReport report);

    Set<Phone> createOrUpdatePhones(Set<Phone> phones, Service service, Location location);

    Eligibility createOrUpdateEligibility(Eligibility eligibility, Service service);

    Set<Language> createOrUpdateLangs(Set<Language> langs, Service service, Location location);

    Set<OpeningHours> createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Service service, Location location);

    ServiceAtLocation createOrUpdateServiceAtLocation(ServiceAtLocation serviceAtLocation, String externalDbId,
                                                      String providerName, Service service, Location location);

    Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName);

    ServiceTaxonomy createOrUpdateServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String externalDbId,
                                                  String providerName, Service service, Taxonomy taxonomy);

    RequiredDocument createOrUpdateRequiredDocument(RequiredDocument requiredDocument, String externalDbId,
                                                    String providerName, Service service);
}
