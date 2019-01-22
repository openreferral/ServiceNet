package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultipleImportData extends ImportData {

    private List<String> multipleObjectsData;
    private List<DocumentUpload> documentUploads;

    public MultipleImportData(List<String> multipleObjectsData, List<DocumentUpload> documentUploads,
                              DataImportReport report, String providerName, boolean isFileUpload) {
        super(report, providerName, isFileUpload);
        this.multipleObjectsData = multipleObjectsData;
        this.documentUploads = documentUploads;
    }
}
