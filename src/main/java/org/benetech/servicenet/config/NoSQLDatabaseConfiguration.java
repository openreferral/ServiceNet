package org.benetech.servicenet.config;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class NoSQLDatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(NoSQLDatabaseConfiguration.class);

    private static final String MONGO_DB_URI = "spring.data.mongodb.uri";

    private static final int TIMEOUT_IN_MS = 500;

    @Autowired
    private Environment env;

    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();
        optionsBuilder.serverSelectionTimeout(TIMEOUT_IN_MS);
        String mongoUri = env.getProperty(MONGO_DB_URI);
        if (mongoUri == null) {
            throw new IllegalStateException(String.format("No property for %s found", MONGO_DB_URI));
        }
        return new SimpleMongoDbFactory(new MongoClientURI(mongoUri, optionsBuilder));
    }
}
