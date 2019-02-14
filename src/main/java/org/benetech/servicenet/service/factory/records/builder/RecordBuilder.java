package org.benetech.servicenet.service.factory.records.builder;

import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.FieldExclusionService;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.benetech.servicenet.service.dto.LocationRecordDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.RecordDTO;
import org.benetech.servicenet.service.dto.ServiceRecordDTO;
import org.benetech.servicenet.service.mapper.ContactMapper;
import org.benetech.servicenet.service.mapper.FieldExclusionMapper;
import org.benetech.servicenet.service.mapper.LocationMapper;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
import org.benetech.servicenet.service.mapper.ServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.benetech.servicenet.service.factory.records.builder.ConflictsBuilder.buildFilteredConflicts;
import static org.benetech.servicenet.service.factory.records.builder.FilteredEntityBuilder.buildCollection;
import static org.benetech.servicenet.service.factory.records.builder.FilteredEntityBuilder.buildObject;

@Component
public class RecordBuilder {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private ExclusionsConfigService exclusionsConfigService;

    @Autowired
    private FieldExclusionService fieldExclusionService;

    @Autowired
    private FieldExclusionMapper exclusionMapper;

    public RecordDTO buildBasicRecord(Organization organization, List<ConflictDTO> conflictDTOS) {
        return new RecordDTO(
            mapOrganization(organization),
            mapLocations(organization.getLocations()),
            mapServices(organization.getServices()),
            mapContacts(organization.getContacts()),
            new HashSet<>(),
            conflictDTOS);
    }

    public RecordDTO buildFilteredRecord(Organization organization, List<ConflictDTO> conflictDTOS,
                                         Set<FieldExclusion> baseExclusions, Set<FieldExclusion> allExclusions)
        throws IllegalAccessException {
        return new RecordDTO(
            mapOrganization(buildObject(organization, Organization.class, allExclusions)),
            mapLocations(buildCollection(organization.getLocations(), Location.class, allExclusions)),
            mapServices(buildCollection(organization.getServices(), Service.class, allExclusions)),
            mapContacts(buildCollection(organization.getContacts(), Contact.class, allExclusions)),
            mapExclusions(baseExclusions),
            buildFilteredConflicts(conflictDTOS, allExclusions));
    }

    private Set<FieldExclusionDTO> mapExclusions(Set<FieldExclusion> exclusions) {
        return exclusions.stream()
            .map(exclusionMapper::toDto).collect(Collectors.toSet());
    }

    private Set<LocationRecordDTO> mapLocations(Set<Location> locations) {
        return locations.stream()
            .map(locationMapper::toRecord)
            .collect(Collectors.toSet());
    }

    private Set<ServiceRecordDTO> mapServices(Set<Service> services) {
        return services.stream()
            .map(serviceMapper::toRecord)
            .collect(Collectors.toSet());
    }

    private Set<ContactDTO> mapContacts(Set<Contact> contacts) {
        return contacts.stream()
            .map(contactMapper::toDto)
            .collect(Collectors.toSet());
    }

    private OrganizationDTO mapOrganization(Organization organization) {
        return organizationMapper.toDto(organization);
    }
}
