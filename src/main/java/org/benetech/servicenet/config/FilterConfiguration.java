package org.benetech.servicenet.config;

import org.benetech.servicenet.filters.RequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public RequestFilter requestFilter() {
        return new RequestFilter();
    }

    @Bean
    public FilterRegistrationBean <RequestFilter> filterRegistrationBean() {
        FilterRegistrationBean <RequestFilter> registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(requestFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
