package org.benetech.servicenet.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.DataImportReportRepository;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.service.DataStatusService;
import org.benetech.servicenet.service.dto.DataStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing DataStatus.
 */
@Service
public class DataStatusServiceImpl implements DataStatusService {

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private DataImportReportRepository dataImportReportRepository;

    @Override
    public Page<DataStatusDto> getDataStatuses(Pageable pageable) {
        List<DataStatusDto> results = new ArrayList<>();
        Page<SystemAccount> systemAccounts = systemAccountRepository.findAll(pageable);
        for (SystemAccount systemAccount : systemAccounts) {
            dataImportReportRepository.findFirstBySystemAccountOrderByEndDateDesc(systemAccount.getName())
                .ifPresent(org -> results.add(this.getDataStatusSto(org, systemAccount)));
        }
        long totalAccounts = systemAccounts.getTotalElements();

        return new PageImpl<>(
            results,
            PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
            totalAccounts
        );
    }

    private DataStatusDto getDataStatusSto(DataImportReport dataImportReport, SystemAccount systemAccount) {
        DataStatusDto dataStatusDto = new DataStatusDto();
        dataStatusDto.setProviderName(systemAccount.getName());
        dataStatusDto.setLastUpdateDateTime(dataImportReport.getEndDate().toLocalDateTime());
        return dataStatusDto;
    }
}
