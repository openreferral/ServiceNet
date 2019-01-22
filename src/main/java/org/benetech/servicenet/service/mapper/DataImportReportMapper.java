package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.dto.DataImportReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity DataImportReport and its DTO DataImportReportDTO.
 */
@Mapper(componentModel = "spring", uses = {DocumentUploadMapper.class}, unmappedTargetPolicy = IGNORE)
public interface DataImportReportMapper extends EntityMapper<DataImportReportDTO, DataImportReport> {

    @Mapping(source = "documentUpload.id", target = "documentUploadId")
    DataImportReportDTO toDto(DataImportReport dataImportReport);

    @Mapping(source = "documentUploadId", target = "documentUpload")
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
