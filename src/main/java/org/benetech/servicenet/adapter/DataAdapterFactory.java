package org.benetech.servicenet.adapter;

import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
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
        try {
            return Optional.of(context.getBean(providerName + DATA_ADAPTER_SUFFIX, SingleDataAdapter.class));
        } catch (NoSuchBeanDefinitionException | BeanNotOfRequiredTypeException e) {
            return Optional.empty();
        }
    }

    public Optional<MultipleDataAdapter> getMultipleDataAdapter(String providerName) {
        try {
            return Optional.of(context.getBean(providerName + DATA_ADAPTER_SUFFIX, MultipleDataAdapter.class));
        } catch (NoSuchBeanDefinitionException | BeanNotOfRequiredTypeException e) {
            return Optional.empty();
        }
    }
}
