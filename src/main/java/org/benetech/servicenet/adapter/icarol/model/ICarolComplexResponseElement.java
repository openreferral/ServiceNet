package org.benetech.servicenet.adapter.icarol.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ICarolComplexResponseElement {

    private static final String PROGRAM = "Program";
    private static final String SITE = "Site";
    private static final String SERVICE_SITE = "ServiceSite";
    private static final String AGENCY = "Agency";
    private static final int BATCH_SIZE = 10;

    private List<List<ICarolSimpleResponseElement>> programBatches;

    private List<List<ICarolSimpleResponseElement>> siteBatches;

    private List<List<ICarolSimpleResponseElement>> serviceSiteBatches;

    private List<List<ICarolSimpleResponseElement>> agencyBatches;

    public ICarolComplexResponseElement(Collection<ICarolSimpleResponseElement> elements) {
        programBatches = getBatch(elements, PROGRAM);
        siteBatches = getBatch(elements, SITE);
        serviceSiteBatches = getBatch(elements, SERVICE_SITE);
        agencyBatches = getBatch(elements, AGENCY);
    }

    private List<List<ICarolSimpleResponseElement>> getBatch(Collection<ICarolSimpleResponseElement> elements, String type) {
        return Lists.partition(elements.stream()
            .filter(e -> e.getType().equals(type)).collect(Collectors.toList()), BATCH_SIZE);
    }
}
