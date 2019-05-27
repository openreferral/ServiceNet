package org.benetech.servicenet.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.repository.PostalAddressRepository;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivityFilterServiceImpl implements ActivityFilterService {

  private final Logger log = LoggerFactory.getLogger(ActivityFilterServiceImpl.class);

  private final PostalAddressRepository postalAddressRepository;

  private final PhysicalAddressRepository physicalAddressRepository;

  private final UserService userService;

  public ActivityFilterServiceImpl(PostalAddressRepository postalAddressRepository,
      PhysicalAddressRepository physicalAddressRepository, UserService userService) {
    this.postalAddressRepository = postalAddressRepository;
    this.physicalAddressRepository = physicalAddressRepository;
    this.userService = userService;
  }

  @Override
  public Set<String> getPostalCodesForUserSystemAccount() {

    Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
    UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

    return Stream
        .of(postalAddressRepository.getDistinctPostalCodesForSystemAccount(systemAccountId),
            physicalAddressRepository.getDistinctPostalCodesForSystemAccount(systemAccountId))
        .flatMap(x -> x.stream())
        .collect(Collectors.toCollection(TreeSet::new));
  }

  @Override
  public Set<String> getRegionsForUserSystemAccount() {

    Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
    UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

    return Stream
        .of(postalAddressRepository.getRegionsForSystemAccount(systemAccountId),
            physicalAddressRepository.getRegionsForSystemAccount(systemAccountId))
        .flatMap(x -> x.stream()).collect(Collectors.toCollection(TreeSet::new));
  }

  @Override
  public Set<String> getCitiesForUserSystemAccount() {

    Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
    UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

    return Stream
        .of(postalAddressRepository.getCitiesForSystemAccount(systemAccountId),
            physicalAddressRepository.getCitiesForSystemAccount(systemAccountId))
        .flatMap(x -> x.stream()).collect(Collectors.toCollection(TreeSet::new));
  }

  @Override
  public Set<String> getPartnersForUserSystemAccount() {

    Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
    UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

    return Stream
        .of(postalAddressRepository.getDistinctPostalCodesForSystemAccount(systemAccountId),
            physicalAddressRepository.getDistinctPostalCodesForSystemAccount(systemAccountId))
        .flatMap(x -> x.stream()).collect(Collectors.toCollection(TreeSet::new));
  }

}
