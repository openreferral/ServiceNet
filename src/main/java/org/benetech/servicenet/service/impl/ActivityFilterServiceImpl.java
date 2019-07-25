package org.benetech.servicenet.service.impl;

import static org.benetech.servicenet.config.Constants.EDEN_PROVIDER;
import static org.benetech.servicenet.config.Constants.UWBA_PROVIDER;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.repository.PostalAddressRepository;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivityFilterServiceImpl implements ActivityFilterService {

    private final PostalAddressRepository postalAddressRepository;

    private final PhysicalAddressRepository physicalAddressRepository;

    private final UserService userService;

    private final TaxonomyRepository taxonomyRepository;

    public ActivityFilterServiceImpl(PostalAddressRepository postalAddressRepository,
        PhysicalAddressRepository physicalAddressRepository, UserService userService,
        TaxonomyRepository taxonomyRepository) {
        this.postalAddressRepository = postalAddressRepository;
        this.physicalAddressRepository = physicalAddressRepository;
        this.userService = userService;
        this.taxonomyRepository = taxonomyRepository;
    }

    @Override
    public Set<String> getPostalCodesForUserSystemAccount() {

        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
        UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

        return Stream
            .of(postalAddressRepository.getDistinctPostalCodesForSystemAccount(systemAccountId),
                physicalAddressRepository.getDistinctPostalCodesForSystemAccount(systemAccountId))
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Set<String> getRegionsForUserSystemAccount() {

        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
        UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

        return Stream
            .of(postalAddressRepository.getRegionsForSystemAccount(systemAccountId),
                physicalAddressRepository.getRegionsForSystemAccount(systemAccountId))
            .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Set<String> getCitiesForUserSystemAccount() {

        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
        UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

        return Stream
            .of(postalAddressRepository.getCitiesForSystemAccount(systemAccountId),
                physicalAddressRepository.getCitiesForSystemAccount(systemAccountId))
            .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
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
