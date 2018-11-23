package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity Phone and its DTO PhoneDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, ServiceMapper.class,
    OrganizationMapper.class, ContactMapper.class, ServiceAtLocationMapper.class})
public interface PhoneMapper extends EntityMapper<PhoneDTO, Phone> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(source = "contact.name", target = "contactName")
    @Mapping(source = "serviceAtLocation.id", target = "serviceAtLocationId")
    PhoneDTO toDto(Phone phone);

    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "srvcId", target = "srvc")
    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "contactId", target = "contact")
    @Mapping(source = "serviceAtLocationId", target = "serviceAtLocation")
    Phone toEntity(PhoneDTO phoneDTO);

    default Phone fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Phone phone = new Phone();
        phone.setId(id);
        return phone;
    }
}
