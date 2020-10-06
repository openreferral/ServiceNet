package org.benetech.servicenet.scheduler;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.benetech.servicenet.adapter.smcconnect.SmcConnectDataMapper;
import org.benetech.servicenet.adapter.smcconnect.model.SmcTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.type.ListType;
import org.benetech.servicenet.util.HttpUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMCConnectTaxonomyUpdateJob extends BaseJob {

    private static final Logger LOG = LoggerFactory.getLogger(SMCConnectTaxonomyUpdateJob.class);

    private static final String NAME = "SMCConnect Taxonomy Update Job";

    private static final String DESCRIPTION = "Collect SMCConnect Taxonomies through the API, map it to the common structure"
        + " and persist it to the database";

    private static final String URL = "https://api.smc-connect.org/categories";

    private static final SmcConnectDataMapper MAPPER = SmcConnectDataMapper.INSTANCE;

    @Value("${scheduler.smc-connect-taxonomies.interval}")
    private int intervalInSeconds;

    @Autowired
    private TaxonomyImportService taxonomyImportService;

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getFullName() {
        return NAME;
    }

    @Override
    public int getIntervalInSeconds() {
        return intervalInSeconds;
    }

    @Override
    public Date getStartTime() {
        return getOffsetDate(60 * 60);
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        LOG.info(getFullName() + " is being executed");

        String response;
        try {
            response = HttpUtils.executeGET(URL, HttpUtils.getStandardHeaders());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with SMC API", e);
        }

        List<Taxonomy> taxonomies = getTaxonomiesFromJson(response).stream()
            .map(MAPPER::extractTaxonomy)
            .collect(Collectors.toList());

        taxonomyImportService.importTaxonomies(taxonomies);
    }

    private List<SmcTaxonomy> getTaxonomiesFromJson(String json) {
        return new Gson().fromJson(json, new ListType<>(SmcTaxonomy.class));
    }
}
