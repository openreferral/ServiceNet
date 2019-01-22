package org.benetech.servicenet.adapter;

import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.web.rest.errors.IncorrectFilesNumberEException;

public abstract class MultipleDataAdapter extends AbstractDataAdapter<MultipleImportData> {

    protected abstract int getNumberOfFilesToProcess();

    /**
     * Each import should start with number of files verification
     * @param data data to be imported
     */
    protected void verifyData(MultipleImportData data) {
        if (data.getDocumentUploads().size() != getNumberOfFilesToProcess()) {
            throw new IncorrectFilesNumberEException(getNumberOfFilesToProcess());
        }
    }
}
