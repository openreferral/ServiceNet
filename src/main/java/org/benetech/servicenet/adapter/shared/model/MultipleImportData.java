package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import org.benetech.servicenet.domain.DataImportReport;

import java.util.List;

@Data
public class MultipleImportData extends ImportData {

    private List<String> multipleObjectsData;

    public MultipleImportData(List<String> multipleObjectsData, DataImportReport report, String providerName) {
        super(report, providerName);
        this.multipleObjectsData = multipleObjectsData;
    }
}
