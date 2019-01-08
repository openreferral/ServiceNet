package org.benetech.servicenet.adapter;

import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.DataImportReport;

public abstract class AbstractDataAdapter<T extends ImportData> {

    /**
     * @param data is an object containing data to be imported
     */
    public abstract DataImportReport importData(T data);
}
