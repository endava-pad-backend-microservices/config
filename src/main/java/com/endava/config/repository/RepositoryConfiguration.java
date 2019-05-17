package com.endava.config.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = "com.endava.config.repository")
public class RepositoryConfiguration extends AbstractMongoConfiguration {

	@Value("${MONGO_URL}")
	private String MONGO_URL;

	@Value("${MONGO_PORT}")
	private int MONGO_PORT;

	@Value("${MONGO_USER}")
	private String MONGO_USER;

	@Value("${MONGO_PASSWORD}")
	private String MONGO_PASSWORD;

	@Value("${MONGO_DB}")
	private String MONGO_DB;

	@Override
	public MongoClient mongoClient() {
		return new MongoClient(Collections.singletonList(new ServerAddress(MONGO_URL, MONGO_PORT)), Collections
				.singletonList(MongoCredential.createCredential(MONGO_DB, MONGO_USER, MONGO_PASSWORD.toCharArray())));
	}

	@Override
	protected String getDatabaseName() {
		return "admin";
	}
}
