package org.benetech.servicenet;

import io.github.jhipster.config.DefaultProfileUtil;
import javax.annotation.PostConstruct;
import org.benetech.servicenet.client.OAuth2InterceptedFeignConfiguration;
import org.benetech.servicenet.config.ApplicationProperties;
import org.benetech.servicenet.scheduler.ReferenceDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

@ComponentScan(
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = OAuth2InterceptedFeignConfiguration.class)
)
@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
@EnableDiscoveryClient
@SuppressWarnings("CPD-START")
public class ReferenceDataGeneratorApp {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceNetApp.class);

    private final Environment env;

    @Autowired
    private ReferenceDataGenerator referenceDataGenerator;

    public ReferenceDataGeneratorApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes ServiceNet.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on
     * <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void generateFakeData() {
        if (env.getProperty("orgs") != null) {
            String userLogin = env.getProperty("login");
            int numberOfOrganizationsToCreate = Integer.parseInt(env.getProperty("orgs"));
            referenceDataGenerator.createReferenceData(userLogin, numberOfOrganizationsToCreate);
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReferenceDataGeneratorApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        LOG.info("Fake Data generator is running");
    }
}
