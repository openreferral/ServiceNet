package org.benetech.servicenet.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.ExceptionPropagationPolicy;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.github.jhipster.security.uaa.LoadBalancedResourceDetails;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;
import org.benetech.servicenet.errors.HystrixBadRequestAlertException;
import org.benetech.servicenet.errors.IdAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;

public class OAuth2InterceptedFeignConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final LoadBalancedResourceDetails loadBalancedResourceDetails;

    private static final int STATUS_400 = 400;

    private static final int STATUS_409 = 409;

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

    public <T> Optional<T> getResponseBody(Response response, Class<T> klass) {
        try {
            String bodyJson = new BufferedReader(new InputStreamReader(response.body().asInputStream()))
                .lines().parallel().collect(Collectors.joining("\n"));
            return Optional.ofNullable(new ObjectMapper().readValue(bodyJson, klass));
        } catch (IOException e) {
            log.error("Error when read feign response.", e);
            return Optional.empty();
        }
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            int status = response.status();
            if (status == STATUS_409) {
                return new IdAlreadyUsedException();
            } else if (status == STATUS_400) {
                Optional<HystrixBadRequestAlertException> exception = getResponseBody(
                    response, HystrixBadRequestAlertException.class);
                if (exception.isPresent()) {
                    return exception.get();
                }
            }
            return new RuntimeException("Response Code " + status);
        };
    }
}
