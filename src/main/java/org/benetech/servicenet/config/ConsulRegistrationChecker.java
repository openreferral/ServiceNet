package org.benetech.servicenet.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.consul.enabled", matchIfMissing = true)
@ConditionalOnConsulEnabled
@ConditionalOnDiscoveryEnabled
public class ConsulRegistrationChecker implements SmartLifecycle {
    public static final String APPLICATION_NAME_KEY = "spring.application.name";
    public static final Long CHECK_INTERVAL_MS = 10000L;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ThreadPoolTaskScheduler taskScheduler = getTaskScheduler();

    private final AtomicBoolean running = new AtomicBoolean(false);

    private ScheduledFuture<?> watchFuture;

    @Autowired
    private Environment environment;

    @Autowired
    private ConsulClient client;

    @Autowired
    private AbstractAutoServiceRegistration serviceRegistration;

    @Autowired
    private ConsulDiscoveryProperties consulDiscoveryProperties;

    public void doCheck() {
        String applicationName = environment.getRequiredProperty(APPLICATION_NAME_KEY);
        String serviceId = consulDiscoveryProperties.getInstanceId().replace(":", "-");

        if (serviceRegistration.isRunning()) {
            try {
                Response<List<CatalogService>> services = this.client.getCatalogService(applicationName, QueryParams.DEFAULT);
                Optional<CatalogService> catalogService = services.getValue().stream()
                    .filter(service -> service.getServiceId().equals(serviceId))
                    .findAny();
                if (!catalogService.isPresent()) {
                    // re-register service
                    log.warn("The {} service is not in the registry, refreshing", applicationName);
                    serviceRegistration.stop();
                    serviceRegistration.start();
                }
            } catch (Exception e) {
                log.error("Couldn't query catalog service for {}", applicationName, e);
            }
        }
    }
    private static ThreadPoolTaskScheduler getTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            this.watchFuture = this.taskScheduler.scheduleWithFixedDelay(
                this::doCheck, CHECK_INTERVAL_MS);
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false) && this.watchFuture != null) {
            this.watchFuture.cancel(true);
        }
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }
}
