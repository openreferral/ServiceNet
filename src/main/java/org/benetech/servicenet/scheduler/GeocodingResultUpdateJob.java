package org.benetech.servicenet.scheduler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.matching.counter.GeoApi;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeocodingResultUpdateJob extends BaseJob {

    private static final String NAME = "Geocoding Update Job";

    private static final String DESCRIPTION = "Update existing Geocoding";

    private final Logger log = LoggerFactory.getLogger(GeocodingResultUpdateJob.class);

    @Autowired
    private GeocodingResultRepository geocodingResultRepository;

    @Autowired
    private GeoApi geoApi;

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
        return Integer.MAX_VALUE;
    }

    @Override
    public Date getStartTime() {
        return getOffsetDate(0);
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info(getFullName() + " is being executed");

        List<GeocodingResult> locationList = geocodingResultRepository
            .findByFormattedAddressIsNullOrLocalityIsNullAndAddressIsNotNull();
        try {
            for (GeocodingResult geocodingResult : locationList) {
                this.updateGeo(geocodingResult.getAddress(), geocodingResult.getId());
            }
            log.info(getFullName() + " executed successfully");
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateGeo(String address, UUID id) {
        Arrays.stream(geoApi.geocode(address))
            .forEach(x -> {
                GeocodingResult geo = new GeocodingResult(address, x);
                geo.setId(id);
                geocodingResultRepository.save(geo);
            });
    }
}
