package com.endava.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.endava.config.annotation.EnableMongoConfigServer;
import com.endava.config.repository.DevRepository;
import com.endava.config.repository.ProdRepository;
import com.endava.config.repository.QARepository;

import lombok.extern.log4j.Log4j2;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoConfigServer
@EnableDiscoveryClient
@EnableSwagger2
@RefreshScope
@Log4j2
@ComponentScan({ "com.endava.config.repository" })
public class ConfigApplication implements CommandLineRunner {

	@Autowired
	private DevRepository devRepository;

	@Autowired
	private QARepository qaRepository;

	@Autowired
	private ProdRepository prodRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException, ParseException, URISyntaxException {
		initializeConfigs();
	}

	private void initializeConfigs() throws IOException, ParseException, URISyntaxException {
		for (Profile profile : Profile.values()) {
				log.debug(String.format("Attempting to insert configs for service %s", profile));
				FileReader reader = new FileReader(getFileFromResources(getConfigFilePath(profile)));
				JSONParser jsonParser = new JSONParser();
				String JSONString = jsonParser.parse(reader).toString();
				log.debug(String.format("Inserting configurations %s", JSONString));
				mongoTemplate.dropCollection(profile.toString().toLowerCase());
				mongoTemplate.save(JSONString, profile.toString().toLowerCase());
		}
	}

	private String getConfigFilePath(Profile profile) {
		return String.format("%s.json", profile.toString().toLowerCase());
	}

	private File getFileFromResources(String filename) throws URISyntaxException, MalformedURLException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();

		URL resource = classLoader.getResource(filename);

		if (resource == null) {
			throw new IllegalArgumentException("File not found!");
		} else {
			URI uri = resource.toURI();
			File file = new File(uri.toURL().getPath());
			return file;
		}
	}

	@Configuration
	@EnableSwagger2
	public class SwaggerConfig {
		@Bean
		public Docket authAPI() {
			return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("*"))
					.paths(PathSelectors.any()).build();
		}
	}

}
