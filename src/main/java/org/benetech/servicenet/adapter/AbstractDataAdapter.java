package org.benetech.servicenet.adapter;

import org.benetech.servicenet.adapter.shared.model.ImportData;

public abstract class AbstractDataAdapter<T extends ImportData> {

    /**
     * @param data is an object containing data to be imported
     */
    public abstract void importData(T data);
}
