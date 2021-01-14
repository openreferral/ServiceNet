package org.benetech.servicenet.adapter.shared.model;

import java.io.File;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.benetech.servicenet.domain.DataImportReport;

@Data
@EqualsAndHashCode(callSuper = true)
public class SingleImportData extends ImportData {

    private String singleObjectData;

    private File data;

    public SingleImportData(String singleObjectsData, DataImportReport report, String providerName,
        boolean isFileUpload) {
        super(report, providerName, isFileUpload);
        this.singleObjectData = singleObjectsData;
    }

    public SingleImportData(File data, DataImportReport report, String providerName,
        boolean isFileUpload) {
        super(report, providerName, isFileUpload);
        this.data = data;
    }
}
