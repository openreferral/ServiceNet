package org.benetech.servicenet.adapter;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class DataAdapterFactory {

    private static final String DATA_ADAPTER_SUFFIX = "DataAdapter";

    private final ApplicationContext context;

    public DataAdapterFactory(ApplicationContext context) {
        this.context = context;
    }

    public Optional<SingleDataAdapter> getSingleDataAdapter(String providerName) {
        AbstractDataAdapter adapter = getDataAdapter(providerName);
        if (adapter instanceof SingleDataAdapter) {
            return Optional.of((SingleDataAdapter) adapter);
        } else {
            return Optional.empty();
        }
    }

    public Optional<MultipleDataAdapter> getMultipleDataAdapter(String providerName) {
        AbstractDataAdapter adapter = getDataAdapter(providerName);
        if (adapter instanceof MultipleDataAdapter) {
            return Optional.of((MultipleDataAdapter) adapter);
        } else {
            return Optional.empty();
        }
    }

    private AbstractDataAdapter getDataAdapter(String providerName) {
        try {
            return context.getBean(providerName + DATA_ADAPTER_SUFFIX, AbstractDataAdapter.class);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
