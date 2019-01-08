package org.benetech.servicenet.adapter.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.benetech.servicenet.domain.DataImportReport;

@Data
@AllArgsConstructor
public class ImportData {

    private DataImportReport report;

    private String providerName;
}
