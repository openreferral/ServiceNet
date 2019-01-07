package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import org.benetech.servicenet.domain.DataImportReport;

@Data
public class SingleImportData extends ImportData {

    private String singleObjectData;

    public SingleImportData(String singleObjectsData, DataImportReport report, String providerName) {
        super(report, providerName);
        this.singleObjectData = singleObjectsData;
    }
}
