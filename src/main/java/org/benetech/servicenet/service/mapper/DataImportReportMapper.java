package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.dto.DataImportReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity DataImportReport and its DTO DataImportReportDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DataImportReportMapper extends EntityMapper<DataImportReportDTO, DataImportReport> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    DataImportReportDTO toDto(DataImportReport dataImportReport);

    @Mapping(source = "userId", target = "user")
    DataImportReport toEntity(DataImportReportDTO dataImportReportDTO);

    default DataImportReport fromId(UUID id) {
        if (id == null) {
            return null;
        }
        DataImportReport dataImportReport = new DataImportReport();
        dataImportReport.setId(id);
        return dataImportReport;
    }
}
