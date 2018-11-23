package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity Contact and its DTO ContactDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, ServiceMapper.class, ServiceAtLocationMapper.class})
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    @Mapping(source = "serviceAtLocation.id", target = "serviceAtLocationId")
    ContactDTO toDto(Contact contact);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "srvcId", target = "srvc")
    @Mapping(source = "serviceAtLocationId", target = "serviceAtLocation")
    Contact toEntity(ContactDTO contactDTO);

    default Contact fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }
}
