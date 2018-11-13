package org.benetech.servicenet.config;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class NoSQLDatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(NoSQLDatabaseConfiguration.class);

    private static final String MONGO_DB_URI = "spring.data.mongodb.uri";

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.connection.timeout}")
    private int timeout;

    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();
        optionsBuilder.serverSelectionTimeout(timeout);
        return new SimpleMongoDbFactory(new MongoClientURI(mongoUri, optionsBuilder));
    }
}
