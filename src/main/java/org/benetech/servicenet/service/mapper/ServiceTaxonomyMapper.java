package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity ServiceTaxonomy and its DTO ServiceTaxonomyDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class, TaxonomyMapper.class})
public interface ServiceTaxonomyMapper extends EntityMapper<ServiceTaxonomyDTO, ServiceTaxonomy> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    @Mapping(source = "taxonomy.id", target = "taxonomyId")
    @Mapping(source = "taxonomy.name", target = "taxonomyName")
    ServiceTaxonomyDTO toDto(ServiceTaxonomy serviceTaxonomy);

    @Mapping(source = "srvcId", target = "srvc")
    @Mapping(source = "taxonomyId", target = "taxonomy")
    ServiceTaxonomy toEntity(ServiceTaxonomyDTO serviceTaxonomyDTO);

    default ServiceTaxonomy fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy();
        serviceTaxonomy.setId(id);
        return serviceTaxonomy;
    }
}
