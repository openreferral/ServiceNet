package org.benetech.servicenet;

import org.benetech.servicenet.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockedUserTestConfiguration {

    @Bean
    @Primary
    public UserService userService() {
        return new TestUserService();
    }
}
