package org.benetech.servicenet.adapter.eden.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EdenComplexResponseElement {

    private static final String PROGRAM = "Program";
    private static final String SITE = "Site";
    private static final String SERVICE_SITE = "ServiceSite";
    private static final String AGENCY = "Agency";
    private static final String PROGRAM_AT_SITE = "ProgramAtSite";
    private static final int BATCH_SIZE = 10;

    private List<List<EdenSimpleResponseElement>> programBatches;

    private List<List<EdenSimpleResponseElement>> siteBatches;

    private List<List<EdenSimpleResponseElement>> serviceSiteBatches;

    private List<List<EdenSimpleResponseElement>> agencyBatches;

    private List<List<EdenSimpleResponseElement>> programAtSiteBatches;

    public EdenComplexResponseElement(Collection<EdenSimpleResponseElement> elements) {
        programBatches = getBatch(elements, PROGRAM);
        siteBatches = getBatch(elements, SITE);
        serviceSiteBatches = getBatch(elements, SERVICE_SITE);
        agencyBatches = getBatch(elements, AGENCY);
        programAtSiteBatches = getBatch(elements, PROGRAM_AT_SITE);
    }

    private List<List<EdenSimpleResponseElement>> getBatch(Collection<EdenSimpleResponseElement> elements, String type) {
        return Lists.partition(elements.stream()
            .filter(e -> e.getType().equals(type)).collect(Collectors.toList()), BATCH_SIZE);
    }
}
