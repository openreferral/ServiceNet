package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import org.benetech.servicenet.domain.DocumentUpload;

import java.util.List;

@Data
public class MultipleImportData extends ImportData {

    private List<String> multipleObjectsData;

    public MultipleImportData(List<String> multipleObjectsData, DocumentUpload documentUpload) {
        super(documentUpload);
        this.multipleObjectsData = multipleObjectsData;
    }
}
