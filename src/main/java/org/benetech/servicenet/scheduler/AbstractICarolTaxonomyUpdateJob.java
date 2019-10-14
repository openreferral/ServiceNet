package org.benetech.servicenet.scheduler;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.icarol.ICarolDataMapper;
import org.benetech.servicenet.adapter.icarol.model.ICarolTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.type.ListType;
import org.benetech.servicenet.util.HttpUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractICarolTaxonomyUpdateJob extends BaseJob {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractICarolTaxonomyUpdateJob.class);

    private static final String URL = "https://api.icarol.com/v1/Resource/Taxonomy";

    private static final ICarolDataMapper MAPPER = ICarolDataMapper.INSTANCE;

    @Autowired
    private TaxonomyImportService taxonomyImportService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        LOG.info(getFullName() + " is being executed");

        Header[] headers = HttpUtils.getStandardAuthHeaders(getApiKey());

        List<ICarolTaxonomy> baseTaxonomies = fetchTaxonomies(URL, headers);
        List<ICarolTaxonomy> allTaxonomies = new ArrayList<>(baseTaxonomies);

        baseTaxonomies.forEach(taxonomy -> {
            allTaxonomies.addAll(fetchTaxonomies(URL + "?id=" + taxonomy.getCode(), headers));
        });

        List<Taxonomy> taxonomies = allTaxonomies.stream()
            .map(taxonomy -> MAPPER.extractTaxonomy(taxonomy, getProviderName()))
            .collect(Collectors.toList());

        taxonomyImportService.importTaxonomies(taxonomies);
    }

    protected abstract String getApiKey();

    protected abstract String getProviderName();

    private List<ICarolTaxonomy> fetchTaxonomies(String url, Header[] headers) {
        String response;
        try {
            response = HttpUtils.executeGET(url, headers);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with iCarol API");
        }

        return getTaxonomiesFromJson(response);
    }

    private List<ICarolTaxonomy> getTaxonomiesFromJson(String json) {
        return new Gson().fromJson(json, new ListType<>(ICarolTaxonomy.class));
    }

}
