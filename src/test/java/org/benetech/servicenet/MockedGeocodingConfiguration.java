package org.benetech.servicenet;

import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.mapper.GeocodingResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockedGeocodingConfiguration {

    @Autowired
    private GeocodingResultRepository geocodingResultRepository;

    @Autowired
    private GeocodingResultMapper geocodingResultMapper;

    @Bean
    @Primary
    public GeocodingResultService geocodingResultService() {
        return new TestGeocodingResultService(geocodingResultRepository, geocodingResultMapper);
    }
}
