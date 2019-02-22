package org.benetech.servicenet;

import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.service.SystemAccountService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.mapper.SystemAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockedUserTestConfiguration {

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private SystemAccountMapper systemAccountMapper;

    @Bean
    @Primary
    public UserService userService() {
        return new TestUserService();
    }

    @Bean
    @Primary
    public SystemAccountService systemAccountService() {
        return new TestSystemAccountService(systemAccountRepository, systemAccountMapper);
    }
}
