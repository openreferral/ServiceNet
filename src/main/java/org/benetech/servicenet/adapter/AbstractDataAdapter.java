package org.benetech.servicenet.adapter;

import java.util.List;

public abstract class AbstractDataAdapter {

    /**
     * @param data list of single objects in JSON format to be mapped and persisted
     */
    public abstract void persistData(String data);

    /**
     * @param data list of multiple objects of different types to be mapped and persisted, all in JSON format
     */
    public abstract void persistData(List<String> data);

    /**
     * @return true if only single objects mapping is supported
     */
    public abstract boolean isUsedForSingleObjects();

    protected static final String SINGLE_MAPPING_NOT_SUPPORTED = "Single objects mapping is not supported";

    protected static final String MULTIPLE_MAPPING_NOT_SUPPORTED = "Multiple different objects mapping is not supported";
}
