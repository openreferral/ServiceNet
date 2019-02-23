package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.benetech.servicenet.domain.DataImportReport;

@Data
@EqualsAndHashCode(callSuper = true)
public class SingleImportData extends ImportData {

    private String singleObjectData;

    public SingleImportData(String singleObjectsData, DataImportReport report, String providerName, boolean isFileUpload,
                            String googleApiKey) {
        super(report, providerName, isFileUpload, googleApiKey);
        this.singleObjectData = singleObjectsData;
    }
}
