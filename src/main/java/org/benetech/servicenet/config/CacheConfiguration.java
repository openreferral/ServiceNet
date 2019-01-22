package org.benetech.servicenet.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.benetech.servicenet.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.User.class.getName() + ".authorities",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.User.class.getName() + ".persistentTokens",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.SystemAccount.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.DocumentUpload.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Organization.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Organization.class.getName() + ".programs",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Organization.class.getName() + ".services",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Organization.class.getName() + ".contacts",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName() + ".areas", jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName() + ".docs", jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName() + ".paymentsAccepteds",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName() + ".langs", jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName() + ".taxonomies",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName() + ".phones",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Service.class.getName() + ".contacts",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Program.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Program.class.getName() + ".services",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.ServiceAtLocation.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.ServiceAtLocation.class.getName() + ".phones",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Location.class.getName() + ".langs", jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Location.class.getName() + ".accessibilities",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.PhysicalAddress.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.PostalAddress.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Phone.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Contact.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.RegularSchedule.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.HolidaySchedule.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Funding.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Eligibility.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.ServiceArea.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.RequiredDocument.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.PaymentAccepted.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Language.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.AccessibilityForDisabilities.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.ServiceTaxonomy.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Taxonomy.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.OrganizationMatch.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Metadata.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.OpeningHours.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.RegularSchedule.class.getName() + ".openingHours",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Conflict.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.Conflict.class.getName() + ".acceptedThisChange",
                jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.DataImportReport.class.getName(), jcacheConfiguration);
            cm.createCache(org.benetech.servicenet.domain.ConfidentialRecord.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
