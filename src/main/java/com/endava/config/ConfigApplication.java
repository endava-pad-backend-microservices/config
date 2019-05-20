package com.endava.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.endava.config.annotation.EnableMongoConfigServer;
import com.endava.config.repository.AuthRepository;
import com.endava.config.repository.GatewayRepository;
import com.endava.config.repository.RegistryRepository;
import com.endava.config.repository.UsersRepository;

import lombok.extern.log4j.Log4j2;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoConfigServer
@EnableDiscoveryClient
@EnableSwagger2
@Log4j2
@ComponentScan({"com.endava.config.repository"})
public class ConfigApplication implements CommandLineRunner {

	@Autowired
	private RegistryRepository registryRepository;
	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private GatewayRepository gatewayRepository;
	@Autowired
	private UsersRepository usersRepository;

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
		for (Service service : Service.values()) {
			int collectionSize = -1;
			switch (service) {
			case AUTH:
				collectionSize = authRepository.findAll().size();
				break;

			case GATEWAY:
				collectionSize = gatewayRepository.findAll().size();
				break;

			case REGISTRY:
				collectionSize = registryRepository.findAll().size();
				break;

			case USERS:
				collectionSize = usersRepository.findAll().size();
				break;

			default:
				log.info("DEFAULTED");
				break;
			}

			if (collectionSize == 0) {
				log.debug(String.format("%s IS EMPTY!!", service));
				log.debug(String.format("Attempting to insert configs for service %s", service));

				FileReader reader = new FileReader(getFileFromResources(getConfigFilePath(service)));
				JSONParser jsonParser = new JSONParser();

				String JSONString = jsonParser.parse(reader).toString();
				log.debug(String.format("Inserting configurations %s", JSONString));

				mongoTemplate.save(JSONString, service.toString().toLowerCase());
			} else {
				log.info(String.format("%s has %d configuration(s).", service, collectionSize));
			}
		}
	}

	private String getConfigFilePath(Service service) {
		return String.format("%s.json", service.toString().toLowerCase());
	}

	private File getFileFromResources(String filename) throws URISyntaxException, MalformedURLException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();

		URL resource = classLoader.getResource(filename);
		
		if (resource == null) {
			throw new IllegalArgumentException("File not found!");
		} else {
			URI uri = resource.toURI();
			try(InputStream inputStream = uri.toURL().openStream()){
				File file = new File(uri.toURL().getPath());
				FileUtils.copyInputStreamToFile(inputStream, file);
				return file;
			}
		}
	}

	@Configuration
	@EnableSwagger2
	public class SwaggerConfig {
		@Bean
		public Docket authAPI() {
			return new Docket(DocumentationType.SWAGGER_2).select()
					.apis(RequestHandlerSelectors.basePackage("com.endava.*")).build();
		}
	}

}
