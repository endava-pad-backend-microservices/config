package com.endava.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.endava.config.annotation.EnableMongoConfigServer;
import com.endava.config.repository.DevRepository;
import com.endava.config.repository.ProdRepository;
import com.endava.config.repository.QARepository;

import org.apache.commons.io.FileUtils;
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
	private MongoTemplate mongoTemplate;

	@Autowired
	private DevRepository devRepository;

	@Autowired
	private QARepository qaRepository;

	@Autowired
	private ProdRepository prodRepository;

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException, ParseException, URISyntaxException {
		initializeConfigs();
	}

	private void initializeConfigs() throws IOException, ParseException, URISyntaxException {
		for (Profile profile : Profile.values()) {
			int collectionSize = -1;
			switch (profile) {
			case DEV:
				collectionSize = devRepository.findAll().size();
				break;

			case QA:
				collectionSize = qaRepository.findAll().size();
				break;

			case PROD:
				collectionSize = prodRepository.findAll().size();
				break;

			default:
				log.info("DEFAULTED");
				break;
			}

			if (collectionSize == 0) {
				log.debug("%s is empty!", profile);
				log.debug(String.format("Attempting to insert configuration to profile %s", profile));
				FileReader reader = new FileReader(getFileFromResources(getConfigFilePath(profile)));
				JSONParser jsonParser = new JSONParser();
				String JSONString = jsonParser.parse(reader).toString();
				log.debug(String.format("Inserting configurations %s", JSONString));
				mongoTemplate.dropCollection(profile.toString().toLowerCase());
				mongoTemplate.save(JSONString, profile.toString().toLowerCase());
			}
		}
	}

	private String getConfigFilePath(Profile profile) {
		return String.format("%s.json", profile.toString().toLowerCase());
	}

	private File getFileFromResources(String filename) throws URISyntaxException, MalformedURLException, IOException {

		// this is the path within the jar file
		InputStream input = ConfigApplication.class.getResourceAsStream(filename);
		if (input == null) {
			// this is how we load file within editor
			input = ConfigApplication.class.getClassLoader().getResourceAsStream(filename);
		}

		try (InputStream is = input) {
			File tempFile = new File(filename);
			FileUtils.copyInputStreamToFile(is, tempFile);
			return tempFile;
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
