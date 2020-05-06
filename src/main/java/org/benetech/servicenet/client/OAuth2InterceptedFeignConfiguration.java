package org.benetech.servicenet.client;

import feign.ExceptionPropagationPolicy;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import io.github.jhipster.security.uaa.LoadBalancedResourceDetails;
import org.benetech.servicenet.errors.IdAlreadyUsedException;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;

public class OAuth2InterceptedFeignConfiguration {

    private final LoadBalancedResourceDetails loadBalancedResourceDetails;

    public OAuth2InterceptedFeignConfiguration(LoadBalancedResourceDetails loadBalancedResourceDetails) {
        this.loadBalancedResourceDetails = loadBalancedResourceDetails;
    }

    @Bean(name = "oauth2RequestInterceptor")
    public RequestInterceptor getOAuth2RequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), loadBalancedResourceDetails);
    }

    @Bean
    ExceptionPropagationPolicy getExceptionPropagationPolicy() {
        return ExceptionPropagationPolicy.UNWRAP;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            int status = response.status();
            if (status == 409) {
                return new IdAlreadyUsedException();
            }
            else {
                return new RuntimeException("Response Code " + status);
            }
        };
    }
}
