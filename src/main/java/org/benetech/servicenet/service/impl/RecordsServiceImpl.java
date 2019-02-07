package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.RecordDTO;
import org.benetech.servicenet.service.factory.records.RecordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecordFactory recordFactory;

    @Override
    public Optional<RecordDTO> getRecordFromOrganization(UUID organizationId, UUID resourceId) {
        return organizationService.findOne(organizationId)
            .flatMap(organization -> userService.getCurrentSystemAccount()
                .flatMap(account -> recordFactory.getFilteredResult(resourceId, organization)));
    }
}
