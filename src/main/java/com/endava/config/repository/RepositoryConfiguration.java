package com.endava.config.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = "com.endava.config.repository")
public class RepositoryConfiguration extends AbstractMongoConfiguration {

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(Collections.singletonList(new ServerAddress("localhost", 27018)),
                Collections.singletonList(MongoCredential.createCredential("admin", "admin",
                        "123456".toCharArray())));
    }

    @Override
    protected String getDatabaseName() {
        return "admin";
    }
}
