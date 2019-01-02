package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import org.benetech.servicenet.domain.DataImportReport;

@Data
public class SingleImportData extends ImportData {

    private String singleObjectData;

    public SingleImportData(String singleObjectsData, DataImportReport report) {
        super(report);
        this.singleObjectData = singleObjectsData;
    }
}
