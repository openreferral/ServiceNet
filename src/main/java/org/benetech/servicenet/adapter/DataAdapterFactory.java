package org.benetech.servicenet.adapter;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class DataAdapterFactory {

    private static final String DATA_ADAPTER_SUFFIX = "DataAdapter";

    private final ApplicationContext applicationContext;

    public DataAdapterFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public AbstractDataAdapter getAdapter(String providerName) {
        try {
            return applicationContext.getBean(providerName + DATA_ADAPTER_SUFFIX, AbstractDataAdapter.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new IllegalArgumentException("No adapter found for " + providerName);
        }
    }
}
