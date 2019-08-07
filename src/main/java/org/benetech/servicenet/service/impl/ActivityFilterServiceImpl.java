package org.benetech.servicenet.service.impl;

import static org.benetech.servicenet.config.Constants.EDEN_PROVIDER;
import static org.benetech.servicenet.config.Constants.UWBA_PROVIDER;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivityFilterServiceImpl implements ActivityFilterService {

    private final GeocodingResultRepository geocodingResultRepository;

    private final UserService userService;

    private final TaxonomyRepository taxonomyRepository;

    public ActivityFilterServiceImpl(GeocodingResultRepository geocodingResultRepository,
        UserService userService,
        TaxonomyRepository taxonomyRepository) {
        this.geocodingResultRepository = geocodingResultRepository;
        this.userService = userService;
        this.taxonomyRepository = taxonomyRepository;
    }

    @Override
    public Set<String> getPostalCodesForUserSystemAccount() {

        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();

        if (accountOpt.isEmpty()) {
            return Collections.emptySet();
        }

        String systemAccountName = accountOpt.get().getName();

        return geocodingResultRepository.getDistinctPostalCodesFromGeoResultsForSystemAccount(systemAccountName);
    }

    @Override
    public Set<String> getRegionsForUserSystemAccount() {

        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();

        if (accountOpt.isEmpty()) {
            return Collections.emptySet();
        }

        String systemAccountName = accountOpt.get().getName();

        return geocodingResultRepository.getDistinctRegionsFromGeoResultsForSystemAccount(systemAccountName);
    }

    @Override
    public Set<String> getCitiesForUserSystemAccount() {

        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();

        if (accountOpt.isEmpty()) {
            return Collections.emptySet();
        }

        String systemAccountName = accountOpt.get().getName();

        return geocodingResultRepository.getDistinctCityFromGeoResultsForSystemAccount(systemAccountName);
    }

    @Override
    public Set<String> getTaxonomiesForUserSystemAccount() {
        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();

        if (accountOpt.isEmpty()) {
            return Collections.emptySet();
        }

        String providerName = accountOpt.get().getName();

        if (UWBA_PROVIDER.equals(providerName) || EDEN_PROVIDER.equals(providerName)) {
            return taxonomyRepository.getICarolTaxonomyNamesForProviderName(providerName);
        }

        return taxonomyRepository.getTaxonomyNamesForProviderName(providerName);
    }
}
