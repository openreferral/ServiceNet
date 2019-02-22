package org.benetech.servicenet;

import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.service.dto.SystemAccountDTO;
import org.benetech.servicenet.service.impl.SystemAccountServiceImpl;
import org.benetech.servicenet.service.mapper.SystemAccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.benetech.servicenet.TestConstants.PROVIDER;

/**
 * Service class used only for tests, with some methods mocked
 */
@Service
@Transactional
public class TestSystemAccountService extends SystemAccountServiceImpl {

    public TestSystemAccountService(SystemAccountRepository systemAccountRepository,
                                    SystemAccountMapper systemAccountMapper) {
        super(systemAccountRepository, systemAccountMapper);
        var account = new SystemAccountDTO();
        account.setName(PROVIDER);
        if (super.findByName(PROVIDER).isEmpty()) {
            super.save(account);
        }
    }
}
