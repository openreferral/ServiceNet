package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import org.benetech.servicenet.domain.DocumentUpload;

@Data
public class SingleImportData extends ImportData {

    private String singleObjectData;

    public SingleImportData(String singleObjectsData, DocumentUpload documentUpload, String providerName) {
        super(documentUpload, providerName);
        this.singleObjectData = singleObjectsData;
    }
}
