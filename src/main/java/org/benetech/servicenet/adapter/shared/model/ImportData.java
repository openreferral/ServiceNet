package org.benetech.servicenet.adapter.shared.model;

import lombok.Data;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.matching.model.MatchingContext;

/**
 * The context resources should be released, when the object wont be used anymore. Use invalidateContext() for that
 */
@Data
public class ImportData {

    private DataImportReport report;

    private String providerName;

    private boolean isFileUpload;

    private MatchingContext context;

    public ImportData(DataImportReport report, String providerName, boolean isFileUpload, String googleApiKey) {
        this.report = report;
        this.providerName = providerName;
        this.isFileUpload = isFileUpload;
        this.context = new MatchingContext(googleApiKey);
    }
}
