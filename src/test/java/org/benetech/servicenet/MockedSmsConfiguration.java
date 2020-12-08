package org.benetech.servicenet;

import org.benetech.servicenet.service.SmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockedSmsConfiguration {

    @Bean
    @Primary
    public SmsService userService() {
        return new TestSmsService();
    }
}
